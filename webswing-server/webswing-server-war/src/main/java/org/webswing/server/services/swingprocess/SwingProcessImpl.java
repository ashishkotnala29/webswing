package org.webswing.server.services.swingprocess;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.jms.IllegalStateException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.webswing.toolkit.util.DeamonThreadFactory;

public class SwingProcessImpl implements SwingProcess {
	private final Logger log;
	private final Logger defaultLog;
	private final ScheduledExecutorService processHandlerThread;
	private static final long LOG_POLLING_PERIOD = 100L;
	
	private final SwingProcessConfig config;
	private Process process;
	private ScheduledFuture<?> logsProcessor;
	private InputStream out;
	private InputStream err;
	private StringBuilder bufferOut = new StringBuilder();
	private StringBuilder bufferErr = new StringBuilder();
	private byte[] buffer = new byte[4096];
	private boolean hasSessionLog;
	private String sessionLogDestination;

	private boolean destroying;
	private ScheduledFuture<?> delayedTermination;
	private boolean forceKilled = false;
	private ProcessExitListener closeListener;

	public SwingProcessImpl(SwingProcessConfig config, ScheduledExecutorService processHandlerThread) {
		super();
		this.config = config;
		
		defaultLog = (Logger) LogManager.getLogger(SwingProcessImpl.class + "_" + config.getApplicationName());
		this.processHandlerThread = processHandlerThread;

		Appender logAppender = config.getLogAppender();
		
		if (config.getLogAppender() != null) {
			log = (Logger) LogManager.getLogger(SwingProcessImpl.class + "_" + config.getApplicationName() + "_" + config.getName()); // because of different log configurations per app, we need a separate logger instance for each app session

			Configuration loggerConfig = ((LoggerContext) LogManager.getContext(false)).getConfiguration();
			loggerConfig.setLoggerAdditive(log, false);
			
			if (logAppender instanceof RollingFileAppender) {
				sessionLogDestination = new File(((RollingFileAppender) logAppender).getFileName()).getAbsolutePath();
			}
			
			log.addAppender(logAppender);
			hasSessionLog = true;
		} else {
			log = defaultLog; // use default appender configuration (webswing.log)
		}
	}

	public void execute() throws Exception {
		if (!isRunning()) {
			ProcessBuilder processBuilder = new ProcessBuilder(buildCommandline());
			if (verifyBaseDir()) {
				processBuilder.directory(new File(config.getBaseDir()));
			}
			
			log.info("Starting application process [" + config.getName() + "] from [" + config.getBaseDir() + "] :" + processBuilder.command());
			if (hasSessionLog) {
				defaultLog.info("Starting application process [" + config.getName() + "] from [" + config.getBaseDir() + "] :" + processBuilder.command());
				defaultLog.info("Logging into: " + sessionLogDestination);
				log.info("Logging into: " + sessionLogDestination);
			}
			
			process = processBuilder.start();
			logsProcessor = processHandlerThread.scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					if (process != null) {
						if (out == null || err == null) {
							out = process.getInputStream();
							err = process.getErrorStream();
						}
						try {
							processStream(out, bufferOut, buffer, config.getName(), false);
							processStream(err, bufferErr, buffer, config.getName(), true);
						} catch (Exception e) {
							log.error("Failed to process process logs for application process " + config.getName(), e);
							destroy();
						}
						if (!SwingProcessImpl.this.isRunning()) {
							destroy();
						}
					}
				}
			}, LOG_POLLING_PERIOD, LOG_POLLING_PERIOD, TimeUnit.MILLISECONDS);
		} else {
			throw new IllegalStateException("Process is already running.");
		}
	}

	private boolean verifyBaseDir() {
		if (config.getBaseDir() == null || config.getBaseDir().isEmpty()) {
			return false;
		} else {
			File file = new File(config.getBaseDir());
			if (file.exists() && file.isDirectory() && file.canRead()) {
				return true;
			} else {
				String error = "";
				if (!file.exists()) {
					error = "Path does not exist.";
				} else if (!file.isDirectory()) {
					error = "Path is not a directory";
				} else if (!file.canRead()) {
					error = "Directory is not accessible";
				}
				throw new IllegalArgumentException("Failed to start application process with base dir:'" + config.getBaseDir() + "'. " + error);
			}
		}
	}

	public void destroy() {
		destroy(0);
	}

	public void destroy(int delayMs) {
		if (delayMs > 0 && delayedTermination == null) {
			log.info("Waiting " + delayMs + "ms for app process " + config.getName() + " to end.");
			delayedTermination = processHandlerThread.schedule(new Runnable() {
				@Override
				public void run() {
					destroy(0);
				}
			}, delayMs, TimeUnit.MILLISECONDS);
		} else if (!destroying) {
			destroying = true;
			try {
				if (delayedTermination != null) {
					delayedTermination.cancel(false);
				}
				destroyInternal();
			} finally {
				logsProcessor.cancel(false);
				log.info("[" + config.getName() + "] app process terminated. ");
				if (hasSessionLog) {
					defaultLog.info("[" + config.getName() + "] app process terminated. ");
				}
				if (getCloseListener() != null) {
					try {
						getCloseListener().onClose();
					} catch (Exception e) {
						log.error("Failed to call onClose", e);
					}
				}
				if (hasSessionLog) {
					log.getAppenders().values().forEach(appender -> appender.stop());
				}
				destroying = false;
			}
		}
	}

	private void destroyInternal() {
		if (isRunning()) {
			log.info("Killing Application process " + config.getName() + ".");
			process.destroy();
			forceKilled = true;
		}
	}

	public boolean isRunning() {
		if (process == null) {
			return false;
		}
		try {
			process.exitValue();
			return false;
		} catch (Exception e) {
			return true;
		}
	}

	private String[] buildCommandline() throws Exception {
		List<String> cmd = new ArrayList<String>();

		if (config.getJreExecutable() == null || config.getJreExecutable().isEmpty()) {
			throw new IllegalArgumentException("JRE executable cannot be empty. Please specify JRE.");
		}
		translateAndAdd(cmd, config.getJreExecutable(), "jreExecutable");
		if (config.getJvmArgs() != null) {
			translateAndAdd(cmd, config.getJvmArgs(), "jvmArgs");
		}
		if (config.getProperties().size() > 0) {
			for (Entry<String, String> entry : config.getProperties().entrySet()) {
				String property = "-D" + entry.getKey();
				String value = entry.getValue();
				if (value != null && !value.isEmpty()) {
					property += "=" + value;
				}
				cmd.add(property);
			}
		}
		if (config.getClassPath() != null) {
			cmd.add("-cp");
			cmd.add(config.getClassPath());
		}
		if (config.getMainClass() != null) {
			cmd.add(config.getMainClass());
		}
		if (config.getArgs() != null) {
			translateAndAdd(cmd, config.getArgs(), "args");
		}
		return cmd.toArray(new String[cmd.size()]);
	}

	private void translateAndAdd(List<String> cmd, String args, String fieldName) throws Exception {
		try {
			for (String s : translateCommandline(args)) {
				cmd.add(s);
			}
		} catch (Exception e) {
			throw new Exception("Illegal value for '" + fieldName + "' field.", e);
		}
	}

	/**
	 * Copy of method from Apache Ant - Commandline class. Crack a command line.
	 * 
	 * @param toProcess
	 *            the command line to process.
	 * @return the command line broken into strings. An empty or null toProcess
	 *         parameter results in a zero sized array.
	 * @throws Exception
	 */
	public static String[] translateCommandline(String toProcess) throws Exception {
		if (toProcess == null || toProcess.length() == 0) {
			// no command? no string
			return new String[0];
		}
		// parse with a simple finite state machine

		final int normal = 0;
		final int inQuote = 1;
		final int inDoubleQuote = 2;
		int state = normal;
		final StringTokenizer tok = new StringTokenizer(toProcess, "\"\' ", true);
		final ArrayList<String> result = new ArrayList<String>();
		final StringBuilder current = new StringBuilder();
		boolean lastTokenHasBeenQuoted = false;

		while (tok.hasMoreTokens()) {
			String nextTok = tok.nextToken();
			switch (state) {
			case inQuote:
				if ("\'".equals(nextTok)) {
					lastTokenHasBeenQuoted = true;
					state = normal;
				} else {
					current.append(nextTok);
				}
				break;
			case inDoubleQuote:
				if ("\"".equals(nextTok)) {
					lastTokenHasBeenQuoted = true;
					state = normal;
				} else {
					current.append(nextTok);
				}
				break;
			default:
				if ("\'".equals(nextTok)) {
					state = inQuote;
				} else if ("\"".equals(nextTok)) {
					state = inDoubleQuote;
				} else if (" ".equals(nextTok)) {
					if (lastTokenHasBeenQuoted || current.length() != 0) {
						result.add(current.toString());
						current.setLength(0);
					}
				} else {
					current.append(nextTok);
				}
				lastTokenHasBeenQuoted = false;
				break;
			}
		}
		if (lastTokenHasBeenQuoted || current.length() != 0) {
			result.add(current.toString());
		}
		if (state == inQuote || state == inDoubleQuote) {
			throw new Exception("unbalanced quotes in " + toProcess);
		}
		return result.toArray(new String[result.size()]);
	}

	private void processStream(InputStream out, StringBuilder bufferOut, byte[] buffer, String name, boolean isError) throws IOException {
		long start = System.currentTimeMillis();
		boolean timeout = false;
		while (out.available() > 0 && !timeout) {
			int available = out.available();
			int read = out.read(buffer, 0, available > buffer.length ? buffer.length : available);
			bufferOut.append(new String(buffer, 0, read));
			while (bufferOut.indexOf("\n") >= 0) {
				int indexofNewLine = bufferOut.indexOf("\n");
				boolean isCR = indexofNewLine > 0 && bufferOut.charAt(indexofNewLine - 1) == '\r';
				String msg = "[" + name + "] " + bufferOut.subSequence(0, isCR ? indexofNewLine - 1 : indexofNewLine);
				if (isError) {
					log.error(msg);
				} else {
					log.info(msg);
				}
				bufferOut.delete(0, indexofNewLine + 1);
			}
			timeout = System.currentTimeMillis() - start > LOG_POLLING_PERIOD ? true : false;
		}
	}

	public boolean isForceKilled() {
		return forceKilled;
	}

	public ProcessExitListener getCloseListener() {
		return closeListener;
	}

	public void setProcessExitListener(ProcessExitListener closeListener) {
		this.closeListener = closeListener;
	}

	@Override
	public SwingProcessConfig getConfig() {
		return config;
	}
}

package org.webswing.server.extension;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.base.PrimaryUrlHandler;
import org.webswing.server.base.UrlHandler;
import org.webswing.server.model.exception.WsInitException;
import org.webswing.server.services.config.ConfigurationService;
import org.webswing.server.services.rest.RestService;
import org.webswing.server.services.rest.RestUrlHandler;
import org.webswing.server.services.swingprocess.SwingProcessService;

import java.lang.reflect.Constructor;
import java.util.List;

@Singleton
public class ExtensionServiceImpl implements ExtensionService, ExtensionDependencies {

	private final ExtensionClassLoader extensionLoader;
	private final SwingProcessService processService;
	private final ConfigurationService configService;
	private final RestService restService;
	private ExtensionProvider provider;
	private Logger logger = LoggerFactory.getLogger(ExtensionServiceImpl.class); 

	@Inject
	public ExtensionServiceImpl(ExtensionClassLoader extensionLoader, SwingProcessService processService, ConfigurationService configuService, RestService restService) {
		this.extensionLoader = extensionLoader;
		this.processService = processService;
		this.configService = configuService;
		this.restService = restService;
	}

	@Override
	public void start() throws WsInitException {
		String providerClassName = System.getProperty(Constants.EXTENSION_PROVIDER, DefaultExtensionProvider.class.getName());
		logger.info("Initializing extension provider {}", providerClassName);
		try {
			Class<?> providerClass = extensionLoader.loadClass(providerClassName);
			try {
				Constructor<?> constructor = providerClass.getDeclaredConstructor(ExtensionDependencies.class);
				provider = (ExtensionProvider) constructor.newInstance(this);
			} catch (NoSuchMethodException e) {
				provider = (ExtensionProvider) providerClass.newInstance();
			}
		} catch (Exception e) {
			logger.error("Could not instantiate extension provider {}", providerClassName, e);
			throw new WsInitException("Could not instantiate extension provider " + providerClassName, e);
		}
	}

	@Override
	public void stop() {
	}

	@Override
	public List<UrlHandler> createExtHandlers(PrimaryUrlHandler parent) {
		return provider.createExtensionHandlers(parent);
	}

	@Override
	public SwingProcessService getProcessService() {
		return processService;
	}

	@Override
	public ConfigurationService getConfigService() {
		return configService;
	}

	@Override
	public RestUrlHandler createRestHandler(UrlHandler parent, Class... resources) {
		return restService.createRestHandler(parent,resources);
	}

	protected ExtensionProvider getProvider() {
		return provider;
	}
}

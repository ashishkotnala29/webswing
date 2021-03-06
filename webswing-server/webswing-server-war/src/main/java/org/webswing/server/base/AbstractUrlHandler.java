package org.webswing.server.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.server.model.exception.WsException;
import org.webswing.server.services.security.SecurableService;
import org.webswing.server.services.security.api.AbstractWebswingUser;
import org.webswing.server.services.security.api.WebswingAction;
import org.webswing.server.services.security.login.SecuredPathHandler;
import org.webswing.server.util.SecurityUtil;
import org.webswing.server.util.ServerUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractUrlHandler implements UrlHandler, SecurableService {
	private static final Logger log = LoggerFactory.getLogger(AbstractUrlHandler.class);

	private final UrlHandler parent;
	private final LinkedList<UrlHandler> childHandlers = new LinkedList<UrlHandler>();

	public AbstractUrlHandler(UrlHandler parent) {
		this.parent = parent;

	}

	@Override
	public void init() {
		synchronized (childHandlers) {
			for (UrlHandler handler : childHandlers) {
				try {
					handler.init();
				} catch (Exception e) {
					log.error("Failed to initialize child handler: " + handler.getClass().getName(), e);
				}
			}
		}
	}

	@Override
	public void destroy() {
		synchronized (childHandlers) {
			for (UrlHandler handler : childHandlers) {
				try {
					handler.destroy();
				} catch (Exception e) {
					log.error("Failed to destroy child handler: " + handler.getClass().getName(), e);
				}
			}
			childHandlers.clear();
		}
	}

	@Override
	public boolean serve(HttpServletRequest req, HttpServletResponse res) throws WsException {
		String pathinfo = getPathInfo(req);
		List<UrlHandler> localHandlerList = new LinkedList<UrlHandler>(childHandlers);
		for (UrlHandler child : localHandlerList) {
			if (isSubPath(toPath(child.getPathMapping()), pathinfo)) {
				boolean served = child.serve(req, res);
				if (served) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Object secureServe(HttpServletRequest req, HttpServletResponse res) throws WsException {
		return serve(req, res);
	}

	public String getFullPathMapping() {
		String handlerPath = toPath(getPathMapping());
		if (this.parent != null) {
			String parentMapping = parent.getFullPathMapping();
			handlerPath = parentMapping + handlerPath;
		} else {
			handlerPath = ServerUtil.getContextPath(getServletContext()) + handlerPath;
		}
		return handlerPath;
	}

	public String getPathInfo(HttpServletRequest req) {
		String fullHandlerPath = getFullPathMapping();
		String requestPath = toPath(ServerUtil.getContextPath(getServletContext()) + req.getPathInfo());
		if (isSubPath(fullHandlerPath, requestPath)) {
			return toPath(requestPath.substring(fullHandlerPath.length()));
		} else {
			return "/";
		}
	}

	protected abstract String getPath();

	public String getPathMapping() {
		String path = toPath(getPath());
		return path;
	}

	public boolean isSubPath(String subpath, String path) {
		return CommonUtil.isSubPath(subpath, path);
	}

	public static String toPath(String path) {
		return CommonUtil.toPath(path);
	}

	public void registerFirstChildUrlHandler(UrlHandler handler) {
		childHandlers.addFirst(handler);
	}

	@Override
	public void registerChildUrlHandler(UrlHandler handler) {
		childHandlers.add(handler);
	}

	@Override
	public void removeChildUrlHandler(UrlHandler handler) {
		if (childHandlers.contains(handler)) {
			childHandlers.remove(handler);
			handler.destroy();
		}
	}

	public ServletContext getServletContext() {
		return parent.getServletContext();
	}

	public String getSecuredPath() {
		if (SecuredPathHandler.class.isAssignableFrom(this.getClass())) {
			SecuredPathHandler provider = (SecuredPathHandler) this;
			if (provider.get() != null) {
				return getFullPathMapping();
			}
		}
		if (parent == null) {
			return getFullPathMapping();
		} else {
			return parent.getSecuredPath();
		}
	}

	public SecuredPathHandler getSecurityProvider() {
		if (SecuredPathHandler.class.isAssignableFrom(this.getClass())) {
			SecuredPathHandler provider = (SecuredPathHandler) this;
			if (provider.get() != null) {
				return provider;
			}
		}
		if (parent == null) {
			return (SecuredPathHandler) this;
		} else {
			return parent.getSecurityProvider();
		}
	}

	public long getLastModified(HttpServletRequest req) {
		return -1;
	}

	public AbstractWebswingUser getUser() {
		return SecurityUtil.getUser(this);
	}

	public AbstractWebswingUser getMasterUser() {
		return SecurityUtil.getUser(getRootHandler());
	}

	@Override
	public UrlHandler getRootHandler() {
		if (parent != null) {
			return parent.getRootHandler();
		} else {
			return this;
		}
	}

	@Override
	public void checkPermission(WebswingAction action) throws WsException {
		AbstractWebswingUser user = getUser();
		checkPermission(user, action);
	}

	@Override
	public void checkMasterPermission(WebswingAction action) throws WsException {
		AbstractWebswingUser user = getMasterUser();
		checkPermission(user, action);
	}

	public void checkPermissionLocalOrMaster(WebswingAction a) throws WsException {
		try {
			checkPermission(a);
		} catch (WsException e) {
			checkMasterPermission(a);
		}
	}

	private void checkPermission(AbstractWebswingUser user, WebswingAction action) throws WsException {
		if (user != null) {
			if (user.isPermitted(action.name())) {
				return;
			}
		}
		throw new WsException("User '" + user + "' is not allowed to execute action '" + action + "'", HttpServletResponse.SC_UNAUTHORIZED);
	}
}

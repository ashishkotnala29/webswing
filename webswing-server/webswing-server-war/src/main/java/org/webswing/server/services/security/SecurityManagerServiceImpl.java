package org.webswing.server.services.security;

import java.util.concurrent.Callable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpServletResponse;
import org.apache.shiro.web.subject.WebSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.extension.EnterpriseExtensionService;
import org.webswing.server.model.exception.WsInitException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SecurityManagerServiceImpl implements SecurityManagerService {
	private static final Logger log = LoggerFactory.getLogger(SecurityManagerServiceImpl.class);

	private final DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

	private ServletContext context;
	private EnterpriseExtensionService extension;

	@Inject
	public SecurityManagerServiceImpl(ServletContext context, EnterpriseExtensionService extension) {
		this.context = context;
		this.extension = extension;
	}

	@Override
	public void start() {
		log.info("Starting SecurityManagerServiceImpl");
		try {
			securityManager.setCacheManager(extension.getSecurityCacheManager());
			securityManager.setSessionManager(new WebswingWebSessionManager());
			securityManager.setRealm(new WebswingRealmAdapter());
			SecurityUtils.setSecurityManager(securityManager);
		} catch (Exception e) {
			log.error("Failed to start security service", e);
			new WsInitException("Failed to start security service", e);
		}
	}

	@Override
	public void stop() {
		log.info("Stopping SecurityManagerServiceImpl");
		securityManager.destroy();
	}

	@Override
	public Object secure(final SecurableService handler, final HttpServletRequest req, final HttpServletResponse res) {
		final ShiroHttpServletRequest request = new ShiroHttpServletRequest(req, context, false);
		final ShiroHttpServletResponse response = new ShiroHttpServletResponse(res, context, request);

		final Subject subject = new WebSubject.Builder(securityManager, request, response).buildWebSubject();

		return subject.execute(new Callable<Object>() {
			public Object call() throws Exception {
				updateSessionLastAccessTime(request, response);
				return handler.secureServe(req, res);
			}
		});
	}

	private void updateSessionLastAccessTime(ShiroHttpServletRequest request, ShiroHttpServletResponse response) {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			Session session = subject.getSession(false);
			if (session != null) {
				try {
					session.touch();
				} catch (Throwable t) {
					log.error("session.touch() method invocation has failed.  Unable to update" + "the corresponding session's last access time based on the incoming request.", t);
				}
			}
		}
	}
}

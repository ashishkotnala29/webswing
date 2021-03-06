package org.webswing.security.modules.keycloak;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.server.services.security.api.AbstractWebswingUser;
import org.webswing.server.services.security.api.SecurityContext;
import org.webswing.server.services.security.api.WebswingAuthenticationException;
import org.webswing.server.services.security.extension.api.WebswingExtendableSecurityModuleConfig;
import org.webswing.security.modules.openidconnect.OpenIdConnectClient;
import org.webswing.server.services.security.modules.AbstractExtendableSecurityModule;
import org.webswing.server.services.security.modules.embeded.EmbededSecurityModule;
import org.webswing.server.services.security.modules.embeded.EmbededSecurityModuleConfig;
import org.webswing.server.services.security.modules.embeded.EmbededUserEntry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KeycloakSecurityModule extends AbstractExtendableSecurityModule<KeycloakSecurityModuleConfig> {
	private static final Logger log = LoggerFactory.getLogger(KeycloakSecurityModule.class);
	public static final String REALM_PARAM = "realm";
	private static final String OIDC_STATE = "KC_OpenIdConnectSessionState";

	private Map<String, OpenIdConnectClient> clients = new HashMap<>();
	private String defaultClient;
	private EmbededSecurityModule fallback;

	public KeycloakSecurityModule(KeycloakSecurityModuleConfig config) {
		super(config);
	}

	public void init() {
		super.init();
		try {
			URL callback = new URL(replaceVar(getConfig().getCallbackUrl()));

			String clientId = replaceVar(getConfig().getClientId());
			String clientSecret = replaceVar(getConfig().getClientSecret());

			String trustedCertsPem = replaceVar(getConfig().getTrustedPemFile());
			File trustedCert = getConfig().getContext().resolveFile(trustedCertsPem);
			boolean disableCertValidation = "DISABLED".equals(trustedCertsPem);

			String roleAttrName = replaceVar(getConfig().getRolesAttributeName());
			String userAttrName = replaceVar(getConfig().getUsernameAttributeName());

			String keycloakUrl = replaceVar(getConfig().getKeycloakUrl());
			List<RealmEntry> realms = getConfig().getRealms();
			if (realms.size() > 0) {
				this.defaultClient = replaceVar(realms.get(0).getRealm());
				for (RealmEntry realm : realms) {
					String currentRealm = replaceVar(realm.getRealm());
					URL discoveryURL = new URL(String.format("%1s/realms/%2s/.well-known/openid-configuration", keycloakUrl, currentRealm));
					URL realmCallback = new URL(CommonUtil.addParam(callback.toString(), REALM_PARAM + "=" + currentRealm));
					OpenIdConnectClient client = new OpenIdConnectClient(discoveryURL, realmCallback, clientId, clientSecret, disableCertValidation, trustedCert, roleAttrName, userAttrName);
					client.setLogoutUrl(replaceVar(realm.getLogoutUrl()));
					clients.put(currentRealm, client);
				}
			} else {
				throw new RuntimeException("No Keycloak realms defined. At least one has to be defined");
			}
			if (getConfig().getFallbackUsers()!=null && getConfig().getFallbackUsers().size()>0) {
				FallbackPropertyConfig fallbackConfig = new FallbackPropertyConfig(getConfig(), getConfig().getFallbackUsers());
				fallback = new EmbededSecurityModule(fallbackConfig);
			}
		} catch (Exception e) {
			log.error("Initializing of OpenID Connect client failed.", e);
			throw new RuntimeException("Initializing of OpenID Connect client failed.", e);
		}
	}

	@Override
	public AbstractWebswingUser doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		OpenIdConnectClient realmClient = clients.get(resolveRealmName(request));
		if (realmClient.isInitialized() || fallback == null) {
			return super.doLogin(request, response);
		} else {
			return fallback.doLogin(request, response);
		}
	}

	private String resolveRealmName(HttpServletRequest request) {
		Map<String, Object> requestData = getLoginRequest(request);
		String r = null;
		if (requestData != null) {
			r = (String) requestData.get(REALM_PARAM);
		}
		return r == null || clients.get(r) == null ? defaultClient : r;
	}

	@Override
	protected void serveLoginPartial(HttpServletRequest request, HttpServletResponse response, WebswingAuthenticationException exception) throws IOException {
		OpenIdConnectClient realmClient = clients.get(resolveRealmName(request));
		String state = UUID.randomUUID().toString().substring(0,7);
		getConfig().getContext().setToSecuritySession(OIDC_STATE, state);
		String url = realmClient.getOpenIDRedirectUrl(state);
		if (url != null) {
			if (exception != null) {
				sendPartialHtml(request, response, "errorPartial.html", exception);
			} else {
				sendRedirect(request, response, url);
			}
		} else {
			sendPartialHtml(request, response, "errorPartial.html", new Exception("Authentication server is not available."));
		}
	}

	@Override
	protected AbstractWebswingUser authenticate(HttpServletRequest request) throws WebswingAuthenticationException {
		String openIdCode = OpenIdConnectClient.getCode(request);
		if (!StringUtils.isEmpty(openIdCode)) {
			try {
				String realmName = resolveRealmName(request);
				OpenIdConnectClient realmClient = clients.get(realmName);
				String expectedState = (String) getConfig().getContext().getFromSecuritySession(OIDC_STATE);
				realmClient.validateCodeRequest(request,expectedState);
				Map<String, Serializable> extraAttr = new HashMap<>();
				extraAttr.put(REALM_PARAM, realmName);
				AbstractWebswingUser user = realmClient.getUser(openIdCode, extraAttr);
				logSuccess(request, user.getUserId());
				return user;
			} catch (Exception e1) {
				logFailure(request, null, "Failed to authenticate." + e1.getMessage());
				log.error("Failed to authenticate", e1);
				throw new WebswingAuthenticationException("Failed to authenticate. " + e1.getMessage(), WebswingAuthenticationException.FAILED_TO_AUTHENTICATE, e1);
			}finally {
				//reset the state value
				getConfig().getContext().setToSecuritySession(OIDC_STATE,null);
			}
		}
		return null;
	}

	@Override
	public void doLogout(HttpServletRequest request, HttpServletResponse response, AbstractWebswingUser user) throws ServletException, IOException {
		String logoutUrl = null;
		String realm = (String) user.getUserAttributes().get(REALM_PARAM);
		if (realm != null && clients.get(realm) != null && clients.get(realm).isInitialized()) {
			logoutUrl = replaceVar(clients.get(realm).getLogoutUrl());
		}
		logoutRedirect(request, response, logoutUrl);
	}

	private class FallbackPropertyConfig implements EmbededSecurityModuleConfig {

		private WebswingExtendableSecurityModuleConfig c;

		List<EmbededUserEntry> users;

		public FallbackPropertyConfig(WebswingExtendableSecurityModuleConfig c, List<EmbededUserEntry> users) {
			this.c = c;
			this.users = users;
		}

		@Override
		public <T> T getValueAs(String name, Class<T> clazz) {
			return c.getValueAs(name, clazz);
		}

		@Override
		public Map<String, Object> asMap() {
			return c.asMap();
		}

		@Override
		public SecurityContext getContext() {
			return c.getContext();
		}

		@Override
		public List<String> getExtensions() {
			return c.getExtensions();
		}

		@Override
		public List<EmbededUserEntry> getUsers() {
			return users;
		}
	}
}

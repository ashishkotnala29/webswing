package org.webswing.server.services.security.modules.property;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.text.PropertiesRealm;
import org.webswing.server.services.security.api.AbstractWebswingUser;
import org.webswing.server.services.security.api.WebswingAuthenticationException;
import org.webswing.server.services.security.modules.AbstractUserPasswordSecurityModule;

public class PropertySecurityModule extends AbstractUserPasswordSecurityModule {

	private PropertiesRealm realm;

	public PropertySecurityModule(PropertySecurityModuleConfig config) {
		super(config);
		realm = new PropertiesRealm();
		String fileName = config.getFile();
		realm.setResourcePath(config.getContext().replaceVariables(fileName));
		realm.onInit();
	}

	@Override
	public AbstractWebswingUser verifyUserPassword(String user, String password) throws WebswingAuthenticationException {
		AuthenticationInfo authtInfo = realm.getAuthenticationInfo(new UsernamePasswordToken(user, password));
		if (authtInfo == null) {
			throw new WebswingAuthenticationException("User not found!");
		}
		return new ShiroWebswingUser(realm, authtInfo);
	}

}

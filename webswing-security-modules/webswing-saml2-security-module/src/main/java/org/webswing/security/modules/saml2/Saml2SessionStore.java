package org.webswing.security.modules.saml2;

import org.pac4j.core.context.session.SessionStore;
import org.webswing.server.services.security.api.SecurityContext;

import java.util.UUID;

public class Saml2SessionStore implements SessionStore<Saml2WebContext> {

	private static final String SESSION_ID = "pac4jSaml2SessionId";
	private final SecurityContext sc;

	public Saml2SessionStore(SecurityContext sc){
		this.sc = sc;
	}

	@Override
	public String getOrCreateSessionId(Saml2WebContext context) {
		String id=null;
		if((id= (String) sc.getFromSecuritySession(SESSION_ID))==null){
			id = UUID.randomUUID().toString();
			sc.setToSecuritySession(SESSION_ID,id);
		}
		return id;
	}

	@Override
	public Object get(Saml2WebContext context, String key) {
		return sc.getFromSecuritySession(key);
	}

	@Override
	public void set(Saml2WebContext context, String key, Object value) {
		sc.setToSecuritySession(key,value);
	}

	@Override
	public boolean destroySession(Saml2WebContext context) {
		return false;
	}

	@Override
	public Object getTrackableSession(Saml2WebContext context) {
		return null;
	}

	@Override
	public SessionStore<Saml2WebContext> buildFromTrackableSession(Saml2WebContext context, Object trackableSession) {
		return null;
	}

	@Override
	public boolean renewSession(Saml2WebContext context) {
		return false;
	}
}

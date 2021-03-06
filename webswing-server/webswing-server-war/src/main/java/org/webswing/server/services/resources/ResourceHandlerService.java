package org.webswing.server.services.resources;

import org.webswing.server.base.PrimaryUrlHandler;
import org.webswing.server.base.UrlHandler;
import org.webswing.server.services.security.api.SecurityContext;

public interface ResourceHandlerService {
	ResourceHandler create(PrimaryUrlHandler manager, SecurityContext context);
}

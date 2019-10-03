package org.webswing.server.services.resources;

import org.webswing.server.base.PrimaryUrlHandler;
import org.webswing.server.base.UrlHandler;
import org.webswing.server.services.security.api.SecurityContext;

import com.google.inject.Singleton;

@Singleton
public class ResourceHandlerServiceImpl implements ResourceHandlerService {

	@Override
	public ResourceHandler create(PrimaryUrlHandler manager, SecurityContext context) {
		return new ResourceHandlerImpl(manager, context);
	}

}

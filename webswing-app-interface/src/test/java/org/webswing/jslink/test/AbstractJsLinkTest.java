package org.webswing.jslink.test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import netscape.javascript.JSObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.webswing.ext.services.JsLinkService;
import org.webswing.ext.services.ServerConnectionService;
import org.webswing.model.MsgOut;
import org.webswing.model.c2s.InputEventsFrameMsgIn;
import org.webswing.model.jslink.JavaEvalRequestMsgIn;
import org.webswing.services.impl.JsLinkServiceImpl;
import org.webswing.toolkit.jslink.WebJSObject;
import org.webswing.toolkit.util.Services;

public abstract class AbstractJsLinkTest {
	private static final ObjectMapper mapper = new ObjectMapper();
	ScriptEngine engine = null;
	JSObject root = new WebJSObject(null);

	public abstract void specificSetUp();

	@Before
	public void setUp() throws Exception {
		JsLinkService jsLinkServiceImpl = JsLinkServiceImpl.getInstance();
		ScriptEngineManager factory = new ScriptEngineManager();
		final ScriptEngine engine = factory.getEngineByName("JavaScript");
		this.engine = engine;
		ServerConnectionService serverServiceImpl = new ServerConnectionService() {

			@Override
			public void sendShutdownNotification() {
			}

			@Override
			public Object sendObjectSync(MsgOut o, String correlationId) throws IOException {
				try {
					engine.put("data", mapper.writeValueAsString(o));
					engine.eval("data=JSON.parse(data)");
					engine.eval("api.jslink.process(data.jsRequest)");
					return mapper.readValue((String) engine.eval("JSON.stringify(result)"), InputEventsFrameMsgIn.class).getJsResponse();
				} catch (Exception e) {
					throw new IOException(e);
				}
			}

			@Override
			public void sendObject(Serializable jsonPaintRequest) {
				try {
					engine.put("data", mapper.writeValueAsString(jsonPaintRequest));
					engine.eval("data=JSON.parse(data)");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
		Services.initialize(null, null, serverServiceImpl, null, null, jsLinkServiceImpl);

		engine.eval("var define =function(array,f){ this[f.name]=f()}");
		engine.eval("self=this; this.setTimeout=function(f,t){f()}");
		engine.eval(new FileReader("../webswing-server/src/main/webapp/javascript/es6promise.js"));
		engine.eval("ES6Promise.polyfill()");
		engine.eval(new FileReader("../webswing-server/src/main/webapp/javascript/webswing-jslink.js"));
		engine.put("sendJava", this);
		engine.eval("var result=null;var window={test:'test'};var api={javaCallTimeout:0,socket:{send:function(obj){result=obj;return result;},awaitResponse:function(callback, request, timeout){sendJava.send(JSON.stringify(request.javaRequest));callback(data.javaResponse);}}}");
		engine.eval("JsLink.init(api)");
		specificSetUp();
	}

	public void send(String obj) throws JsonParseException, JsonMappingException, IOException {
		WebJSObject.evaluateJava(mapper.readValue(obj, JavaEvalRequestMsgIn.class));
	}

}

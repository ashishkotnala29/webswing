(function(a,b){if(typeof define==="function"&&define.amd){define(b)
}else{a.atmosphere=b()
}}(this,function(){var c="2.2.6-javascript",a={},e,d=false,h=[],g=[],f=0,b=Object.prototype.hasOwnProperty;
a={onError:function(i){},onClose:function(i){},onOpen:function(i){},onReopen:function(i){},onMessage:function(i){},onReconnect:function(j,i){},onMessagePublished:function(i){},onTransportFailure:function(j,i){},onLocalMessage:function(i){},onFailureToReconnect:function(j,i){},onClientTimeout:function(i){},onOpenAfterResume:function(i){},WebsocketApiAdapter:function(j){var i,k;
j.onMessage=function(l){k.onmessage({data:l.responseBody})
};
j.onMessagePublished=function(l){k.onmessage({data:l.responseBody})
};
j.onOpen=function(l){k.onopen(l)
};
k={close:function(){i.close()
},send:function(l){i.push(l)
},onmessage:function(l){},onopen:function(l){},onclose:function(l){},onerror:function(l){}};
i=new a.subscribe(j);
return k
},AtmosphereRequest:function(ad){var q={timeout:300000,method:"GET",headers:{},contentType:"",callback:null,url:"",data:"",suspend:true,maxRequest:-1,reconnect:true,maxStreamingLength:10000000,lastIndex:0,logLevel:"info",requestCount:0,fallbackMethod:"GET",fallbackTransport:"streaming",transport:"long-polling",webSocketImpl:null,webSocketBinaryType:null,dispatchUrl:null,webSocketPathDelimiter:"@@",enableXDR:false,rewriteURL:false,attachHeadersAsQueryString:true,executeCallbackBeforeReconnect:false,readyState:0,withCredentials:false,trackMessageLength:false,messageDelimiter:"|",connectTimeout:-1,reconnectInterval:0,dropHeaders:true,uuid:0,async:true,shared:false,readResponsesHeaders:false,maxReconnectOnClose:5,enableProtocol:true,pollingInterval:0,heartbeat:{client:null,server:null},ackInterval:0,closeAsync:false,reconnectOnServerError:true,onError:function(aH){},onClose:function(aH){},onOpen:function(aH){},onMessage:function(aH){},onReopen:function(aI,aH){},onReconnect:function(aI,aH){},onMessagePublished:function(aH){},onTransportFailure:function(aI,aH){},onLocalMessage:function(aH){},onFailureToReconnect:function(aI,aH){},onClientTimeout:function(aH){},onOpenAfterResume:function(aH){}};
var at={status:200,reasonPhrase:"OK",responseBody:"",messages:[],headers:[],state:"messageReceived",transport:"polling",error:null,request:null,partialMessage:"",errorHandled:false,closedByClientTimeout:false,ffTryingReconnect:false};
var ax=null;
var ag=null;
var A=null;
var o=null;
var Y=null;
var v=true;
var az=0;
var K=0;
var ak=" ";
var ap=false;
var R=null;
var i;
var ay=null;
var S=a.util.now();
var z;
var aG;
ao(ad);
function aj(){v=true;
ap=false;
az=0;
ax=null;
ag=null;
A=null;
o=null
}function V(){l();
aj()
}function x(aH){if(aH=="debug"){return q.logLevel==="debug"
}else{if(aH=="info"){return q.logLevel==="info"||q.logLevel==="debug"
}else{if(aH=="warn"){return q.logLevel==="warn"||q.logLevel==="info"||q.logLevel==="debug"
}else{if(aH=="error"){return q.logLevel==="error"||q.logLevel==="warn"||q.logLevel==="info"||q.logLevel==="debug"
}else{return false
}}}}}function J(aI,aH){if(at.partialMessage===""&&(aH.transport==="streaming")&&(aI.responseText.length>aH.maxStreamingLength)){return true
}return false
}function E(){if(q.enableProtocol&&!q.firstMessage){var aJ="X-Atmosphere-Transport=close&X-Atmosphere-tracking-id="+q.uuid;
a.util.each(q.headers,function(aL,aN){var aM=a.util.isFunction(aN)?aN.call(this,q,q,at):aN;
if(aM!=null){aJ+="&"+encodeURIComponent(aL)+"="+encodeURIComponent(aM)
}});
var aH=q.url.replace(/([?&])_=[^&]*/,aJ);
aH=aH+(aH===q.url?(/\?/.test(q.url)?"&":"?")+aJ:"");
var aI={connected:false};
var aK=new a.AtmosphereRequest(aI);
aK.attachHeadersAsQueryString=false;
aK.dropHeaders=true;
aK.url=aH;
aK.contentType="text/plain";
aK.transport="polling";
aK.method="GET";
aK.data="";
if(q.enableXDR){aK.enableXDR=q.enableXDR
}aK.async=q.closeAsync;
am("",aK)
}}function I(){if(x("debug")){a.util.debug("Closing")
}ap=true;
if(q.reconnectId){clearTimeout(q.reconnectId);
delete q.reconnectId
}if(q.heartbeatTimer){clearTimeout(q.heartbeatTimer)
}q.reconnect=false;
at.request=q;
at.state="unsubscribe";
at.responseBody="";
at.status=408;
at.partialMessage="";
ai();
E();
l()
}function l(){at.partialMessage="";
if(q.id){clearTimeout(q.id)
}if(q.heartbeatTimer){clearTimeout(q.heartbeatTimer)
}if(o!=null){o.close();
o=null
}if(Y!=null){Y.abort();
Y=null
}if(A!=null){A.abort();
A=null
}if(ax!=null){if(ax.canSendMessage){ax.close()
}ax=null
}if(ag!=null){ag.close();
ag=null
}ah()
}function ah(){if(i!=null){clearInterval(z);
document.cookie=aG+"=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/";
i.signal("close",{reason:"",heir:!ap?S:(i.get("children")||[])[0]});
i.close()
}if(ay!=null){ay.close()
}}function ao(aH){V();
q=a.util.extend(q,aH);
q.mrequest=q.reconnect;
if(!q.reconnect){q.reconnect=true
}}function av(){return q.webSocketImpl!=null||window.WebSocket||window.MozWebSocket
}function au(){return window.EventSource
}function aa(){if(q.shared){ay=aE(q);
if(ay!=null){if(x("debug")){a.util.debug("Storage service available. All communication will be local")
}if(ay.open(q)){return
}}if(x("debug")){a.util.debug("No Storage service available.")
}ay=null
}q.firstMessage=f==0?true:false;
q.isOpen=false;
q.ctime=a.util.now();
if(q.uuid===0){q.uuid=f
}at.closedByClientTimeout=false;
if(q.transport!=="websocket"&&q.transport!=="sse"){M(q)
}else{if(q.transport==="websocket"){if(!av()){aA("Websocket is not supported, using request.fallbackTransport ("+q.fallbackTransport+")")
}else{af(false)
}}else{if(q.transport==="sse"){if(!au()){aA("Server Side Events(SSE) is not supported, using request.fallbackTransport ("+q.fallbackTransport+")")
}else{D(false)
}}}}}function aE(aL){var aM,aK,aP,aH="atmosphere-"+aL.url,aI={storage:function(){function aQ(aU){if(aU.key===aH&&aU.newValue){aJ(aU.newValue)
}}if(!a.util.storage){return
}var aT=window.localStorage,aR=function(aU){return a.util.parseJSON(aT.getItem(aH+"-"+aU))
},aS=function(aU,aV){aT.setItem(aH+"-"+aU,a.util.stringifyJSON(aV))
};
return{init:function(){aS("children",aR("children").concat([S]));
a.util.on(window,"storage",aQ);
return aR("opened")
},signal:function(aU,aV){aT.setItem(aH,a.util.stringifyJSON({target:"p",type:aU,data:aV}))
},close:function(){var aU=aR("children");
a.util.off(window,"storage",aQ);
if(aU){if(aN(aU,aL.id)){aS("children",aU)
}}}}
},windowref:function(){var aQ=window.open("",aH.replace(/\W/g,""));
if(!aQ||aQ.closed||!aQ.callbacks){return
}return{init:function(){aQ.callbacks.push(aJ);
aQ.children.push(S);
return aQ.opened
},signal:function(aR,aS){if(!aQ.closed&&aQ.fire){aQ.fire(a.util.stringifyJSON({target:"p",type:aR,data:aS}))
}},close:function(){if(!aP){aN(aQ.callbacks,aJ);
aN(aQ.children,S)
}}}
}};
function aN(aT,aS){var aQ,aR=aT.length;
for(aQ=0;
aQ<aR;
aQ++){if(aT[aQ]===aS){aT.splice(aQ,1)
}}return aR!==aT.length
}function aJ(aQ){var aS=a.util.parseJSON(aQ),aR=aS.data;
if(aS.target==="c"){switch(aS.type){case"open":W("opening","local",q);
break;
case"close":if(!aP){aP=true;
if(aR.reason==="aborted"){I()
}else{if(aR.heir===S){aa()
}else{setTimeout(function(){aa()
},100)
}}}break;
case"message":m(aR,"messageReceived",200,aL.transport);
break;
case"localMessage":G(aR);
break
}}}function aO(){var aQ=new RegExp("(?:^|; )("+encodeURIComponent(aH)+")=([^;]*)").exec(document.cookie);
if(aQ){return a.util.parseJSON(decodeURIComponent(aQ[2]))
}}aM=aO();
if(!aM||a.util.now()-aM.ts>1000){return
}aK=aI.storage()||aI.windowref();
if(!aK){return
}return{open:function(){var aQ;
z=setInterval(function(){var aR=aM;
aM=aO();
if(!aM||aR.ts===aM.ts){aJ(a.util.stringifyJSON({target:"c",type:"close",data:{reason:"error",heir:aR.heir}}))
}},1000);
aQ=aK.init();
if(aQ){setTimeout(function(){W("opening","local",aL)
},50)
}return aQ
},send:function(aQ){aK.signal("send",aQ)
},localSend:function(aQ){aK.signal("localSend",a.util.stringifyJSON({id:S,event:aQ}))
},close:function(){if(!ap){clearInterval(z);
aK.signal("close");
aK.close()
}}}
}function aF(){var aI,aH="atmosphere-"+q.url,aM={storage:function(){function aN(aP){if(aP.key===aH&&aP.newValue){aJ(aP.newValue)
}}if(!a.util.storage){return
}var aO=window.localStorage;
return{init:function(){a.util.on(window,"storage",aN)
},signal:function(aP,aQ){aO.setItem(aH,a.util.stringifyJSON({target:"c",type:aP,data:aQ}))
},get:function(aP){return a.util.parseJSON(aO.getItem(aH+"-"+aP))
},set:function(aP,aQ){aO.setItem(aH+"-"+aP,a.util.stringifyJSON(aQ))
},close:function(){a.util.off(window,"storage",aN);
aO.removeItem(aH);
aO.removeItem(aH+"-opened");
aO.removeItem(aH+"-children")
}}
},windowref:function(){var aO=aH.replace(/\W/g,""),aN=document.getElementById(aO),aP;
if(!aN){aN=document.createElement("div");
aN.id=aO;
aN.style.display="none";
aN.innerHTML='<iframe name="'+aO+'" />';
document.body.appendChild(aN)
}aP=aN.firstChild.contentWindow;
return{init:function(){aP.callbacks=[aJ];
aP.fire=function(aQ){var aR;
for(aR=0;
aR<aP.callbacks.length;
aR++){aP.callbacks[aR](aQ)
}}
},signal:function(aQ,aR){if(!aP.closed&&aP.fire){aP.fire(a.util.stringifyJSON({target:"c",type:aQ,data:aR}))
}},get:function(aQ){return !aP.closed?aP[aQ]:null
},set:function(aQ,aR){if(!aP.closed){aP[aQ]=aR
}},close:function(){}}
}};
function aJ(aN){var aP=a.util.parseJSON(aN),aO=aP.data;
if(aP.target==="p"){switch(aP.type){case"send":u(aO);
break;
case"localSend":G(aO);
break;
case"close":I();
break
}}}R=function aL(aN){aI.signal("message",aN)
};
function aK(){document.cookie=aG+"="+encodeURIComponent(a.util.stringifyJSON({ts:a.util.now()+1,heir:(aI.get("children")||[])[0]}))+"; path=/"
}aI=aM.storage()||aM.windowref();
aI.init();
if(x("debug")){a.util.debug("Installed StorageService "+aI)
}aI.set("children",[]);
if(aI.get("opened")!=null&&!aI.get("opened")){aI.set("opened",false)
}aG=encodeURIComponent(aH);
aK();
z=setInterval(aK,1000);
i=aI
}function W(aJ,aM,aI){if(q.shared&&aM!=="local"){aF()
}if(i!=null){i.set("opened",true)
}aI.close=function(){I()
};
if(az>0&&aJ==="re-connecting"){aI.isReopen=true;
r(at)
}else{if(at.error==null){at.request=aI;
var aK=at.state;
at.state=aJ;
var aH=at.transport;
at.transport=aM;
var aL=at.responseBody;
ai();
at.responseBody=aL;
at.state=aK;
at.transport=aH
}}}function aC(aJ){aJ.transport="jsonp";
var aI=q,aH;
if((aJ!=null)&&(typeof(aJ)!=="undefined")){aI=aJ
}Y={open:function(){var aM="atmosphere"+(++S);
function aK(){aI.lastIndex=0;
if(aI.openId){clearTimeout(aI.openId)
}if(aI.heartbeatTimer){clearTimeout(aI.heartbeatTimer)
}if(aI.reconnect&&az++<aI.maxReconnectOnClose){W("re-connecting",aI.transport,aI);
an(Y,aI,aJ.reconnectInterval);
aI.openId=setTimeout(function(){Z(aI)
},aI.reconnectInterval+1000)
}else{U(0,"maxReconnectOnClose reached")
}}function aL(){var aN=aI.url;
if(aI.dispatchUrl!=null){aN+=aI.dispatchUrl
}var aP=aI.data;
if(aI.attachHeadersAsQueryString){aN=p(aI);
if(aP!==""){aN+="&X-Atmosphere-Post-Body="+encodeURIComponent(aP)
}aP=""
}var aO=document.head||document.getElementsByTagName("head")[0]||document.documentElement;
aH=document.createElement("script");
aH.src=aN+"&jsonpTransport="+aM;
aH.clean=function(){aH.clean=aH.onerror=aH.onload=aH.onreadystatechange=null;
if(aH.parentNode){aH.parentNode.removeChild(aH)
}if(++aJ.scriptCount===2){aJ.scriptCount=1;
aK()
}};
aH.onload=aH.onreadystatechange=function(){if(!aH.readyState||/loaded|complete/.test(aH.readyState)){aH.clean()
}};
aH.onerror=function(){aJ.scriptCount=1;
aH.clean()
};
aO.insertBefore(aH,aO.firstChild)
}window[aM]=function(aP){aJ.scriptCount=0;
if(aI.reconnect&&aI.maxRequest===-1||aI.requestCount++<aI.maxRequest){if(!aI.executeCallbackBeforeReconnect){an(Y,aI,aI.pollingInterval)
}if(aP!=null&&typeof aP!=="string"){try{aP=aP.message
}catch(aO){}}var aN=s(aP,aI,at);
if(!aN){m(at.responseBody,"messageReceived",200,aI.transport)
}if(aI.executeCallbackBeforeReconnect){an(Y,aI,aI.pollingInterval)
}k(aI)
}else{a.util.log(q.logLevel,["JSONP reconnect maximum try reached "+q.requestCount]);
U(0,"maxRequest reached")
}};
setTimeout(function(){aL()
},50)
},abort:function(){if(aH&&aH.clean){aH.clean()
}}};
Y.open()
}function aw(aH){if(q.webSocketImpl!=null){return q.webSocketImpl
}else{if(window.WebSocket){return new WebSocket(aH)
}else{return new MozWebSocket(aH)
}}}function w(){return p(q,a.util.getAbsoluteURL(q.webSocketUrl||q.url)).replace(/^http/,"ws")
}function T(){var aH=p(q);
return aH
}function D(aI){at.transport="sse";
var aH=T();
if(x("debug")){a.util.debug("Invoking executeSSE");
a.util.debug("Using URL: "+aH)
}if(aI&&!q.reconnect){if(ag!=null){l()
}return
}try{ag=new EventSource(aH,{withCredentials:q.withCredentials})
}catch(aJ){U(0,aJ);
aA("SSE failed. Downgrading to fallback transport and resending");
return
}if(q.connectTimeout>0){q.id=setTimeout(function(){if(!aI){l()
}},q.connectTimeout)
}ag.onopen=function(aK){k(q);
if(x("debug")){a.util.debug("SSE successfully opened")
}if(!q.enableProtocol){if(!aI){W("opening","sse",q)
}else{W("re-opening","sse",q)
}}else{if(q.isReopen){q.isReopen=false;
W("re-opening",q.transport,q)
}}aI=true;
if(q.method==="POST"){at.state="messageReceived";
ag.send(q.data)
}};
ag.onmessage=function(aL){k(q);
if(!q.enableXDR&&aL.origin&&aL.origin!==window.location.protocol+"//"+window.location.host){a.util.log(q.logLevel,["Origin was not "+window.location.protocol+"//"+window.location.host]);
return
}at.state="messageReceived";
at.status=200;
aL=aL.data;
var aK=s(aL,q,at);
if(!aK){ai();
at.responseBody="";
at.messages=[]
}};
ag.onerror=function(aK){clearTimeout(q.id);
if(q.heartbeatTimer){clearTimeout(q.heartbeatTimer)
}if(at.closedByClientTimeout){return
}ae(aI);
l();
if(ap){a.util.log(q.logLevel,["SSE closed normally"])
}else{if(!aI){aA("SSE failed. Downgrading to fallback transport and resending")
}else{if(q.reconnect&&(at.transport==="sse")){if(az++<q.maxReconnectOnClose){W("re-connecting",q.transport,q);
if(q.reconnectInterval>0){q.reconnectId=setTimeout(function(){D(true)
},q.reconnectInterval)
}else{D(true)
}at.responseBody="";
at.messages=[]
}else{a.util.log(q.logLevel,["SSE reconnect maximum try reached "+az]);
U(0,"maxReconnectOnClose reached")
}}}}}
}function af(aI){at.transport="websocket";
var aH=w(q.url);
if(x("debug")){a.util.debug("Invoking executeWebSocket");
a.util.debug("Using URL: "+aH)
}if(aI&&!q.reconnect){if(ax!=null){l()
}return
}ax=aw(aH);
if(q.webSocketBinaryType!=null){ax.binaryType=q.webSocketBinaryType
}if(q.connectTimeout>0){q.id=setTimeout(function(){if(!aI){var aL={code:1002,reason:"",wasClean:false};
ax.onclose(aL);
try{l()
}catch(aM){}return
}},q.connectTimeout)
}ax.onopen=function(aM){k(q);
d=false;
if(x("debug")){a.util.debug("Websocket successfully opened")
}var aL=aI;
if(ax!=null){ax.canSendMessage=true
}if(!q.enableProtocol){aI=true;
if(aL){W("re-opening","websocket",q)
}else{W("opening","websocket",q)
}}if(ax!=null){if(q.method==="POST"){at.state="messageReceived";
ax.send(q.data)
}}};
ax.onmessage=function(aN){k(q);
if(q.enableProtocol){aI=true
}at.state="messageReceived";
at.status=200;
aN=aN.data;
var aL=typeof(aN)==="string";
if(aL){var aM=s(aN,q,at);
if(!aM){ai();
at.responseBody="";
at.messages=[]
}}else{aN=t(q,aN);
if(aN===""){return
}at.responseBody=aN;
ai();
at.responseBody=null
}};
ax.onerror=function(aL){clearTimeout(q.id);
if(q.heartbeatTimer){clearTimeout(q.heartbeatTimer)
}};
ax.onclose=function(aL){clearTimeout(q.id);
if(at.state==="closed"){return
}var aM=aL.reason;
if(aM===""){switch(aL.code){case 1000:aM="Normal closure; the connection successfully completed whatever purpose for which it was created.";
break;
case 1001:aM="The endpoint is going away, either because of a server failure or because the browser is navigating away from the page that opened the connection.";
break;
case 1002:aM="The endpoint is terminating the connection due to a protocol error.";
break;
case 1003:aM="The connection is being terminated because the endpoint received data of a type it cannot accept (for example, a text-only endpoint received binary data).";
break;
case 1004:aM="The endpoint is terminating the connection because a data frame was received that is too large.";
break;
case 1005:aM="Unknown: no status code was provided even though one was expected.";
break;
case 1006:aM="Connection was closed abnormally (that is, with no close frame being sent).";
break
}}if(x("warn")){a.util.warn("Websocket closed, reason: "+aM);
a.util.warn("Websocket closed, wasClean: "+aL.wasClean)
}if(at.closedByClientTimeout||d){if(q.reconnectId){clearTimeout(q.reconnectId);
delete q.reconnectId
}return
}ae(aI);
at.state="closed";
if(ap){a.util.log(q.logLevel,["Websocket closed normally"])
}else{if(!aI){aA("Websocket failed. Downgrading to Comet and resending")
}else{if(q.reconnect&&at.transport==="websocket"&&aL.code!==1001){l();
if(az++<q.maxReconnectOnClose){W("re-connecting",q.transport,q);
if(q.reconnectInterval>0){q.reconnectId=setTimeout(function(){at.responseBody="";
at.messages=[];
af(true)
},q.reconnectInterval)
}else{at.responseBody="";
at.messages=[];
af(true)
}}else{a.util.log(q.logLevel,["Websocket reconnect maximum try reached "+q.requestCount]);
if(x("warn")){a.util.warn("Websocket error, reason: "+aL.reason)
}U(0,"maxReconnectOnClose reached")
}}}}};
var aJ=navigator.userAgent.toLowerCase();
var aK=aJ.indexOf("android")>-1;
if(aK&&ax.url===undefined){ax.onclose({reason:"Android 4.1 does not support websockets.",wasClean:false})
}}function t(aL,aK){var aJ=aK;
if(aL.transport==="polling"){return aJ
}if(a.util.trim(aK).length!==0&&aL.enableProtocol&&aL.firstMessage){var aM=aL.trackMessageLength?1:0;
var aI=aK.split(aL.messageDelimiter);
if(aI.length<=aM+1){return aJ
}aL.firstMessage=false;
aL.uuid=a.util.trim(aI[aM]);
if(aI.length<=aM+2){a.util.log("error",["Protocol data not sent by the server. If you enable protocol on client side, be sure to install JavascriptProtocol interceptor on server side.Also note that atmosphere-runtime 2.2+ should be used."])
}K=parseInt(a.util.trim(aI[aM+1]),10);
ak=aI[aM+2];
if(aL.transport!=="long-polling"){Z(aL)
}f=aL.uuid;
aJ="";
aM=aL.trackMessageLength?4:3;
if(aI.length>aM+1){for(var aH=aM;
aH<aI.length;
aH++){aJ+=aI[aH];
if(aH+1!==aI.length){aJ+=aL.messageDelimiter
}}}if(aL.ackInterval!==0){setTimeout(function(){u("...ACK...")
},aL.ackInterval)
}}else{if(aL.enableProtocol&&aL.firstMessage&&a.util.browser.msie&&+a.util.browser.version.split(".")[0]<10){a.util.log(q.logLevel,["Receiving unexpected data from IE"])
}else{Z(aL)
}}return aJ
}function k(aH){clearTimeout(aH.id);
if(aH.timeout>0&&aH.transport!=="polling"){aH.id=setTimeout(function(){aD(aH);
E();
l()
},aH.timeout)
}}function aD(aH){at.closedByClientTimeout=true;
at.state="closedByClient";
at.responseBody="";
at.status=408;
at.messages=[];
ai()
}function U(aH,aI){l();
clearTimeout(q.id);
at.state="error";
at.reasonPhrase=aI;
at.responseBody="";
at.status=aH;
at.messages=[];
ai()
}function s(aL,aK,aH){aL=t(aK,aL);
if(aL.length===0){return true
}aH.responseBody=aL;
if(aK.trackMessageLength){aL=aH.partialMessage+aL;
var aJ=[];
var aI=aL.indexOf(aK.messageDelimiter);
if(aI!=-1){while(aI!==-1){var aN=aL.substring(0,aI);
var aM=+aN;
if(isNaN(aM)){throw new Error('message length "'+aN+'" is not a number')
}aI+=aK.messageDelimiter.length;
if(aI+aM>aL.length){aI=-1
}else{aJ.push(aL.substring(aI,aI+aM));
aL=aL.substring(aI+aM,aL.length);
aI=aL.indexOf(aK.messageDelimiter)
}}aH.partialMessage=aL;
if(aJ.length!==0){aH.responseBody=aJ.join(aK.messageDelimiter);
aH.messages=aJ;
return false
}else{aH.responseBody="";
aH.messages=[];
return true
}}}aH.responseBody=aL;
aH.messages=[aL];
return false
}function aA(aH){a.util.log(q.logLevel,[aH]);
if(typeof(q.onTransportFailure)!=="undefined"){q.onTransportFailure(aH,q)
}else{if(typeof(a.util.onTransportFailure)!=="undefined"){a.util.onTransportFailure(aH,q)
}}q.transport=q.fallbackTransport;
var aI=q.connectTimeout===-1?0:q.connectTimeout;
if(q.reconnect&&q.transport!=="none"||q.transport==null){q.method=q.fallbackMethod;
at.transport=q.fallbackTransport;
q.fallbackTransport="none";
if(aI>0){q.reconnectId=setTimeout(function(){aa()
},aI)
}else{aa()
}}else{U(500,"Unable to reconnect with fallback transport")
}}function p(aJ,aH){var aI=q;
if((aJ!=null)&&(typeof(aJ)!=="undefined")){aI=aJ
}if(aH==null){aH=aI.url
}if(!aI.attachHeadersAsQueryString){return aH
}if(aH.indexOf("X-Atmosphere-Framework")!==-1){return aH
}aH+=(aH.indexOf("?")!==-1)?"&":"?";
aH+="X-Atmosphere-tracking-id="+aI.uuid;
aH+="&X-Atmosphere-Framework="+c;
aH+="&X-Atmosphere-Transport="+aI.transport;
if(aI.trackMessageLength){aH+="&X-Atmosphere-TrackMessageSize=true"
}if(aI.heartbeat!==null&&aI.heartbeat.server!==null){aH+="&X-Heartbeat-Server="+aI.heartbeat.server
}if(aI.contentType!==""){aH+="&Content-Type="+(aI.transport==="websocket"?aI.contentType:encodeURIComponent(aI.contentType))
}if(aI.enableProtocol){aH+="&X-atmo-protocol=true"
}a.util.each(aI.headers,function(aK,aM){var aL=a.util.isFunction(aM)?aM.call(this,aI,aJ,at):aM;
if(aL!=null){aH+="&"+encodeURIComponent(aK)+"="+encodeURIComponent(aL)
}});
return aH
}function Z(aH){if(!aH.isOpen){aH.isOpen=true;
W("opening",aH.transport,aH)
}else{if(aH.isReopen){aH.isReopen=false;
W("re-opening",aH.transport,aH)
}else{if(at.state==="messageReceived"&&(aH.transport==="jsonp"||aH.transport==="long-polling")){aq(at)
}else{return
}}}C(aH)
}function C(aI){if(aI.heartbeatTimer!=null){clearTimeout(aI.heartbeatTimer)
}if(!isNaN(K)&&K>0){var aH=function(){if(x("debug")){a.util.debug("Sending heartbeat")
}u(ak);
aI.heartbeatTimer=setTimeout(aH,K)
};
aI.heartbeatTimer=setTimeout(aH,K)
}}function M(aK){var aI=q;
if((aK!=null)||(typeof(aK)!=="undefined")){aI=aK
}aI.lastIndex=0;
aI.readyState=0;
if((aI.transport==="jsonp")||((aI.enableXDR)&&(a.util.checkCORSSupport()))){aC(aI);
return
}if(a.util.browser.msie&&+a.util.browser.version.split(".")[0]<10){if((aI.transport==="streaming")){if(aI.enableXDR&&window.XDomainRequest){Q(aI)
}else{aB(aI)
}return
}if((aI.enableXDR)&&(window.XDomainRequest)){Q(aI);
return
}}var aL=function(){aI.lastIndex=0;
if(aI.reconnect&&az++<aI.maxReconnectOnClose){at.ffTryingReconnect=true;
W("re-connecting",aK.transport,aK);
an(aJ,aI,aK.reconnectInterval)
}else{U(0,"maxReconnectOnClose reached")
}};
var aH=function(){at.errorHandled=true;
l();
aL()
};
if(aI.force||(aI.reconnect&&(aI.maxRequest===-1||aI.requestCount++<aI.maxRequest))){aI.force=false;
var aJ=a.util.xhr();
aJ.hasData=false;
N(aJ,aI,true);
if(aI.suspend){A=aJ
}if(aI.transport!=="polling"){at.transport=aI.transport;
aJ.onabort=function(){ae(true)
};
aJ.onerror=function(){at.error=true;
at.ffTryingReconnect=true;
try{at.status=XMLHttpRequest.status
}catch(aN){at.status=500
}if(!at.status){at.status=500
}if(!at.errorHandled){l();
aL()
}}
}aJ.onreadystatechange=function(){if(ap){return
}at.error=null;
var aO=false;
var aU=false;
if(aI.transport==="streaming"&&aI.readyState>2&&aJ.readyState===4){l();
aL();
return
}aI.readyState=aJ.readyState;
if(aI.transport==="streaming"&&aJ.readyState>=3){aU=true
}else{if(aI.transport==="long-polling"&&aJ.readyState===4){aU=true
}}k(q);
if(aI.transport!=="polling"){var aN=200;
if(aJ.readyState===4){aN=aJ.status>1000?0:aJ.status
}if(!aI.reconnectOnServerError&&(aN>=300&&aN<600)){U(aN,aJ.statusText);
return
}if(aN>=300||aN===0){aH();
return
}if((!aI.enableProtocol||!aK.firstMessage)&&aJ.readyState===2){if(a.util.browser.mozilla&&at.ffTryingReconnect){at.ffTryingReconnect=false;
setTimeout(function(){if(!at.ffTryingReconnect){Z(aI)
}},500)
}else{Z(aI)
}}}else{if(aJ.readyState===4){aU=true
}}if(aU){var aR=aJ.responseText;
at.errorHandled=false;
if(a.util.trim(aR).length===0&&aI.transport==="long-polling"){if(!aJ.hasData){an(aJ,aI,aI.pollingInterval)
}else{aJ.hasData=false
}return
}aJ.hasData=true;
H(aJ,q);
if(aI.transport==="streaming"){if(!a.util.browser.opera){var aQ=aR.substring(aI.lastIndex,aR.length);
aO=s(aQ,aI,at);
aI.lastIndex=aR.length;
if(aO){return
}}else{a.util.iterate(function(){if(at.status!==500&&aJ.responseText.length>aI.lastIndex){try{at.status=aJ.status;
at.headers=a.util.parseHeaders(aJ.getAllResponseHeaders());
H(aJ,q)
}catch(aW){at.status=404
}k(q);
at.state="messageReceived";
var aV=aJ.responseText.substring(aI.lastIndex);
aI.lastIndex=aJ.responseText.length;
aO=s(aV,aI,at);
if(!aO){ai()
}if(J(aJ,aI)){L(aJ,aI);
return
}}else{if(at.status>400){aI.lastIndex=aJ.responseText.length;
return false
}}},0)
}}else{aO=s(aR,aI,at)
}var aT=J(aJ,aI);
try{at.status=aJ.status;
at.headers=a.util.parseHeaders(aJ.getAllResponseHeaders());
H(aJ,aI)
}catch(aS){at.status=404
}if(aI.suspend){at.state=at.status===0?"closed":"messageReceived"
}else{at.state="messagePublished"
}var aP=!aT&&aK.transport!=="streaming"&&aK.transport!=="polling";
if(aP&&!aI.executeCallbackBeforeReconnect){an(aJ,aI,aI.pollingInterval)
}if(at.responseBody.length!==0&&!aO){ai()
}if(aP&&aI.executeCallbackBeforeReconnect){an(aJ,aI,aI.pollingInterval)
}if(aT){L(aJ,aI)
}}};
try{aJ.send(aI.data);
v=true
}catch(aM){a.util.log(aI.logLevel,["Unable to connect to "+aI.url]);
U(0,aM)
}}else{if(aI.logLevel==="debug"){a.util.log(aI.logLevel,["Max re-connection reached."])
}U(0,"maxRequest reached")
}}function L(aI,aH){at.messages=[];
aH.isReopen=true;
I();
ap=false;
an(aI,aH,500)
}function N(aJ,aK,aI){var aH=aK.url;
if(aK.dispatchUrl!=null&&aK.method==="POST"){aH+=aK.dispatchUrl
}aH=p(aK,aH);
aH=a.util.prepareURL(aH);
if(aI){aJ.open(aK.method,aH,aK.async);
if(aK.connectTimeout>0){aK.id=setTimeout(function(){if(aK.requestCount===0){l();
m("Connect timeout","closed",200,aK.transport)
}},aK.connectTimeout)
}}if(q.withCredentials&&q.transport!=="websocket"){if("withCredentials" in aJ){aJ.withCredentials=true
}}if(!q.dropHeaders){aJ.setRequestHeader("X-Atmosphere-Framework",a.util.version);
aJ.setRequestHeader("X-Atmosphere-Transport",aK.transport);
if(aK.heartbeat!==null&&aK.heartbeat.server!==null){aJ.setRequestHeader("X-Heartbeat-Server",aJ.heartbeat.server)
}if(aK.trackMessageLength){aJ.setRequestHeader("X-Atmosphere-TrackMessageSize","true")
}aJ.setRequestHeader("X-Atmosphere-tracking-id",aK.uuid);
a.util.each(aK.headers,function(aL,aN){var aM=a.util.isFunction(aN)?aN.call(this,aJ,aK,aI,at):aN;
if(aM!=null){aJ.setRequestHeader(aL,aM)
}})
}if(aK.contentType!==""){aJ.setRequestHeader("Content-Type",aK.contentType)
}}function an(aI,aJ,aK){if(at.closedByClientTimeout){return
}if(aJ.reconnect||(aJ.suspend&&v)){var aH=0;
if(aI&&aI.readyState>1){aH=aI.status>1000?0:aI.status
}at.status=aH===0?204:aH;
at.reason=aH===0?"Server resumed the connection or down.":"OK";
clearTimeout(aJ.id);
if(aJ.reconnectId){clearTimeout(aJ.reconnectId);
delete aJ.reconnectId
}if(aK>0){q.reconnectId=setTimeout(function(){M(aJ)
},aK)
}else{M(aJ)
}}}function r(aH){aH.state="re-connecting";
al(aH)
}function aq(aH){aH.state="openAfterResume";
al(aH);
aH.state="messageReceived"
}function Q(aH){if(aH.transport!=="polling"){o=ac(aH);
o.open()
}else{ac(aH).open()
}}function ac(aJ){var aI=q;
if((aJ!=null)&&(typeof(aJ)!=="undefined")){aI=aJ
}var aO=aI.transport;
var aN=0;
var aH=new window.XDomainRequest();
var aL=function(){if(aI.transport==="long-polling"&&(aI.reconnect&&(aI.maxRequest===-1||aI.requestCount++<aI.maxRequest))){aH.status=200;
Q(aI)
}};
var aM=aI.rewriteURL||function(aQ){var aP=/(?:^|;\s*)(JSESSIONID|PHPSESSID)=([^;]*)/.exec(document.cookie);
switch(aP&&aP[1]){case"JSESSIONID":return aQ.replace(/;jsessionid=[^\?]*|(\?)|$/,";jsessionid="+aP[2]+"$1");
case"PHPSESSID":return aQ.replace(/\?PHPSESSID=[^&]*&?|\?|$/,"?PHPSESSID="+aP[2]+"&").replace(/&$/,"")
}return aQ
};
aH.onprogress=function(){aK(aH)
};
aH.onerror=function(){if(aI.transport!=="polling"){l();
if(az++<aI.maxReconnectOnClose){if(aI.reconnectInterval>0){aI.reconnectId=setTimeout(function(){W("re-connecting",aJ.transport,aJ);
Q(aI)
},aI.reconnectInterval)
}else{W("re-connecting",aJ.transport,aJ);
Q(aI)
}}else{U(0,"maxReconnectOnClose reached")
}}};
aH.onload=function(){};
var aK=function(aP){clearTimeout(aI.id);
var aR=aP.responseText;
aR=aR.substring(aN);
aN+=aR.length;
if(aO!=="polling"){k(aI);
var aQ=s(aR,aI,at);
if(aO==="long-polling"&&a.util.trim(aR).length===0){return
}if(aI.executeCallbackBeforeReconnect){aL()
}if(!aQ){m(at.responseBody,"messageReceived",200,aO)
}if(!aI.executeCallbackBeforeReconnect){aL()
}}};
return{open:function(){var aP=aI.url;
if(aI.dispatchUrl!=null){aP+=aI.dispatchUrl
}aP=p(aI,aP);
aH.open(aI.method,aM(aP));
if(aI.method==="GET"){aH.send()
}else{aH.send(aI.data)
}if(aI.connectTimeout>0){aI.id=setTimeout(function(){if(aI.requestCount===0){l();
m("Connect timeout","closed",200,aI.transport)
}},aI.connectTimeout)
}},close:function(){aH.abort()
}}
}function aB(aH){o=ab(aH);
o.open()
}function ab(aK){var aJ=q;
if((aK!=null)&&(typeof(aK)!=="undefined")){aJ=aK
}var aI;
var aL=new window.ActiveXObject("htmlfile");
aL.open();
aL.close();
var aH=aJ.url;
if(aJ.dispatchUrl!=null){aH+=aJ.dispatchUrl
}if(aJ.transport!=="polling"){at.transport=aJ.transport
}return{open:function(){var aM=aL.createElement("iframe");
aH=p(aJ);
if(aJ.data!==""){aH+="&X-Atmosphere-Post-Body="+encodeURIComponent(aJ.data)
}aH=a.util.prepareURL(aH);
aM.src=aH;
aL.body.appendChild(aM);
var aN=aM.contentDocument||aM.contentWindow.document;
aI=a.util.iterate(function(){try{if(!aN.firstChild){return
}var aQ=aN.body?aN.body.lastChild:aN;
var aS=function(){var aU=aQ.cloneNode(true);
aU.appendChild(aN.createTextNode("."));
var aT=aU.innerText;
aT=aT.substring(0,aT.length-1);
return aT
};
if(!aN.body||!aN.body.firstChild||aN.body.firstChild.nodeName.toLowerCase()!=="pre"){var aP=aN.head||aN.getElementsByTagName("head")[0]||aN.documentElement||aN;
var aO=aN.createElement("script");
aO.text="document.write('<plaintext>')";
aP.insertBefore(aO,aP.firstChild);
aP.removeChild(aO);
aQ=aN.body.lastChild
}if(aJ.closed){aJ.isReopen=true
}aI=a.util.iterate(function(){var aU=aS();
if(aU.length>aJ.lastIndex){k(q);
at.status=200;
at.error=null;
aQ.innerText="";
var aT=s(aU,aJ,at);
if(aT){return""
}m(at.responseBody,"messageReceived",200,aJ.transport)
}aJ.lastIndex=0;
if(aN.readyState==="complete"){ae(true);
W("re-connecting",aJ.transport,aJ);
if(aJ.reconnectInterval>0){aJ.reconnectId=setTimeout(function(){aB(aJ)
},aJ.reconnectInterval)
}else{aB(aJ)
}return false
}},null);
return false
}catch(aR){at.error=true;
W("re-connecting",aJ.transport,aJ);
if(az++<aJ.maxReconnectOnClose){if(aJ.reconnectInterval>0){aJ.reconnectId=setTimeout(function(){aB(aJ)
},aJ.reconnectInterval)
}else{aB(aJ)
}}else{U(0,"maxReconnectOnClose reached")
}aL.execCommand("Stop");
aL.close();
return false
}})
},close:function(){if(aI){aI()
}aL.execCommand("Stop");
ae(true)
}}
}function u(aH){if(ay!=null){F(aH)
}else{if(A!=null||ag!=null){P(aH)
}else{if(o!=null){j(aH)
}else{if(Y!=null){B(aH)
}else{if(ax!=null){X(aH)
}else{U(0,"No suspended connection available");
a.util.error("No suspended connection available. Make sure atmosphere.subscribe has been called and request.onOpen invoked before invoking this method")
}}}}}}function am(aI,aH){if(!aH){aH=y(aI)
}aH.transport="polling";
aH.method="GET";
aH.withCredentials=false;
aH.reconnect=false;
aH.force=true;
aH.suspend=false;
aH.timeout=1000;
M(aH)
}function F(aH){ay.send(aH)
}function ar(aI){if(aI.length===0){return
}try{if(ay){ay.localSend(aI)
}else{if(i){i.signal("localMessage",a.util.stringifyJSON({id:S,event:aI}))
}}}catch(aH){a.util.error(aH)
}}function P(aI){var aH=y(aI);
M(aH)
}function j(aI){if(q.enableXDR&&a.util.checkCORSSupport()){var aH=y(aI);
aH.reconnect=false;
aC(aH)
}else{P(aI)
}}function B(aH){P(aH)
}function O(aH){var aI=aH;
if(typeof(aI)==="object"){aI=aH.data
}return aI
}function y(aI){var aJ=O(aI);
var aH={connected:false,timeout:60000,method:"POST",url:q.url,contentType:q.contentType,headers:q.headers,reconnect:true,callback:null,data:aJ,suspend:false,maxRequest:-1,logLevel:"info",requestCount:0,withCredentials:q.withCredentials,async:q.async,transport:"polling",isOpen:true,attachHeadersAsQueryString:true,enableXDR:q.enableXDR,uuid:q.uuid,dispatchUrl:q.dispatchUrl,enableProtocol:false,messageDelimiter:"|",trackMessageLength:q.trackMessageLength,maxReconnectOnClose:q.maxReconnectOnClose,heartbeatTimer:q.heartbeatTimer,heartbeat:q.heartbeat};
if(typeof(aI)==="object"){aH=a.util.extend(aH,aI)
}return aH
}function X(aH){var aK=a.util.isBinary(aH)?aH:O(aH);
var aI;
try{if(q.dispatchUrl!=null){aI=q.webSocketPathDelimiter+q.dispatchUrl+q.webSocketPathDelimiter+aK
}else{aI=aK
}if(!ax.canSendMessage){a.util.error("WebSocket not connected.");
return
}ax.send(aI)
}catch(aJ){ax.onclose=function(aL){};
l();
aA("Websocket failed. Downgrading to Comet and resending "+aH);
P(aH)
}}function G(aI){var aH=a.util.parseJSON(aI);
if(aH.id!==S){if(typeof(q.onLocalMessage)!=="undefined"){q.onLocalMessage(aH.event)
}else{if(typeof(a.util.onLocalMessage)!=="undefined"){a.util.onLocalMessage(aH.event)
}}}}function m(aK,aH,aI,aJ){at.responseBody=aK;
at.transport=aJ;
at.status=aI;
at.state=aH;
ai()
}function H(aH,aJ){if(!aJ.readResponsesHeaders){if(!aJ.enableProtocol){aJ.uuid=S
}}else{try{var aI=aH.getResponseHeader("X-Atmosphere-tracking-id");
if(aI&&aI!=null){aJ.uuid=aI.split(" ").pop()
}}catch(aK){}}}function al(aH){n(aH,q);
n(aH,a.util)
}function n(aI,aJ){switch(aI.state){case"messageReceived":az=0;
if(typeof(aJ.onMessage)!=="undefined"){aJ.onMessage(aI)
}if(typeof(aJ.onmessage)!=="undefined"){aJ.onmessage(aI)
}break;
case"error":if(typeof(aJ.onError)!=="undefined"){aJ.onError(aI)
}if(typeof(aJ.onerror)!=="undefined"){aJ.onerror(aI)
}break;
case"opening":delete q.closed;
if(typeof(aJ.onOpen)!=="undefined"){aJ.onOpen(aI)
}if(typeof(aJ.onopen)!=="undefined"){aJ.onopen(aI)
}break;
case"messagePublished":if(typeof(aJ.onMessagePublished)!=="undefined"){aJ.onMessagePublished(aI)
}break;
case"re-connecting":if(typeof(aJ.onReconnect)!=="undefined"){aJ.onReconnect(q,aI)
}break;
case"closedByClient":if(typeof(aJ.onClientTimeout)!=="undefined"){aJ.onClientTimeout(q)
}break;
case"re-opening":delete q.closed;
if(typeof(aJ.onReopen)!=="undefined"){aJ.onReopen(q,aI)
}break;
case"fail-to-reconnect":if(typeof(aJ.onFailureToReconnect)!=="undefined"){aJ.onFailureToReconnect(q,aI)
}break;
case"unsubscribe":case"closed":var aH=typeof(q.closed)!=="undefined"?q.closed:false;
if(!aH){if(typeof(aJ.onClose)!=="undefined"){aJ.onClose(aI)
}if(typeof(aJ.onclose)!=="undefined"){aJ.onclose(aI)
}}q.closed=true;
break;
case"openAfterResume":if(typeof(aJ.onOpenAfterResume)!=="undefined"){aJ.onOpenAfterResume(q)
}break
}}function ae(aH){if(at.state!=="closed"){at.state="closed";
at.responseBody="";
at.messages=[];
at.status=!aH?501:200;
ai()
}}function ai(){var aJ=function(aM,aN){aN(at)
};
if(ay==null&&R!=null){R(at.responseBody)
}q.reconnect=q.mrequest;
var aH=typeof(at.responseBody)==="string";
var aK=(aH&&q.trackMessageLength)?(at.messages.length>0?at.messages:[""]):new Array(at.responseBody);
for(var aI=0;
aI<aK.length;
aI++){if(aK.length>1&&aK[aI].length===0){continue
}at.responseBody=(aH)?a.util.trim(aK[aI]):aK[aI];
if(ay==null&&R!=null){R(at.responseBody)
}if((at.responseBody.length===0||(aH&&ak===at.responseBody))&&at.state==="messageReceived"){continue
}al(at);
if(g.length>0){if(x("debug")){a.util.debug("Invoking "+g.length+" global callbacks: "+at.state)
}try{a.util.each(g,aJ)
}catch(aL){a.util.log(q.logLevel,["Callback exception"+aL])
}}if(typeof(q.callback)==="function"){if(x("debug")){a.util.debug("Invoking request callbacks")
}try{q.callback(at)
}catch(aL){a.util.log(q.logLevel,["Callback exception"+aL])
}}}}this.subscribe=function(aH){ao(aH);
aa()
};
this.execute=function(){aa()
};
this.close=function(){I()
};
this.disconnect=function(){E()
};
this.getUrl=function(){return q.url
};
this.push=function(aJ,aI){if(aI!=null){var aH=q.dispatchUrl;
q.dispatchUrl=aI;
u(aJ);
q.dispatchUrl=aH
}else{u(aJ)
}};
this.getUUID=function(){return q.uuid
};
this.pushLocal=function(aH){ar(aH)
};
this.enableProtocol=function(aH){return q.enableProtocol
};
this.init=function(){aj()
};
this.request=q;
this.response=at
}};
a.subscribe=function(i,l,k){if(typeof(l)==="function"){a.addCallback(l)
}if(typeof(i)!=="string"){k=i
}else{k.url=i
}f=((typeof(k)!=="undefined")&&typeof(k.uuid)!=="undefined")?k.uuid:0;
var j=new a.AtmosphereRequest(k);
j.execute();
h[h.length]=j;
return j
};
a.unsubscribe=function(){if(h.length>0){var j=[].concat(h);
for(var l=0;
l<j.length;
l++){var k=j[l];
k.close();
clearTimeout(k.response.request.id);
if(k.heartbeatTimer){clearTimeout(k.heartbeatTimer)
}}}h=[];
g=[]
};
a.unsubscribeUrl=function(k){var j=-1;
if(h.length>0){for(var m=0;
m<h.length;
m++){var l=h[m];
if(l.getUrl()===k){l.close();
clearTimeout(l.response.request.id);
if(l.heartbeatTimer){clearTimeout(l.heartbeatTimer)
}j=m;
break
}}}if(j>=0){h.splice(j,1)
}};
a.addCallback=function(i){if(a.util.inArray(i,g)===-1){g.push(i)
}};
a.removeCallback=function(j){var i=a.util.inArray(j,g);
if(i!==-1){g.splice(i,1)
}};
a.util={browser:{},parseHeaders:function(j){var i,l=/^(.*?):[ \t]*([^\r\n]*)\r?$/mg,k={};
while(i=l.exec(j)){k[i[1]]=i[2]
}return k
},now:function(){return new Date().getTime()
},isArray:function(i){return Object.prototype.toString.call(i)==="[object Array]"
},inArray:function(l,m){if(!Array.prototype.indexOf){var j=m.length;
for(var k=0;
k<j;
++k){if(m[k]===l){return k
}}return -1
}return m.indexOf(l)
},isBinary:function(i){return/^\[object\s(?:Blob|ArrayBuffer|.+Array)\]$/.test(Object.prototype.toString.call(i))
},isFunction:function(i){return Object.prototype.toString.call(i)==="[object Function]"
},getAbsoluteURL:function(i){var j=document.createElement("div");
j.innerHTML='<a href="'+i+'"/>';
return encodeURI(decodeURI(j.firstChild.href))
},prepareURL:function(j){var k=a.util.now();
var i=j.replace(/([?&])_=[^&]*/,"$1_="+k);
return i+(i===j?(/\?/.test(j)?"&":"?")+"_="+k:"")
},trim:function(i){if(!String.prototype.trim){return i.toString().replace(/(?:(?:^|\n)\s+|\s+(?:$|\n))/g,"").replace(/\s+/g," ")
}else{return i.toString().trim()
}},param:function(m){var k,i=[];
function l(n,o){o=a.util.isFunction(o)?o():(o==null?"":o);
i.push(encodeURIComponent(n)+"="+encodeURIComponent(o))
}function j(o,p){var n;
if(a.util.isArray(p)){a.util.each(p,function(r,q){if(/\[\]$/.test(o)){l(o,q)
}else{j(o+"["+(typeof q==="object"?r:"")+"]",q)
}})
}else{if(Object.prototype.toString.call(p)==="[object Object]"){for(n in p){j(o+"["+n+"]",p[n])
}}else{l(o,p)
}}}for(k in m){j(k,m[k])
}return i.join("&").replace(/%20/g,"+")
},storage:function(){try{return !!(window.localStorage&&window.StorageEvent)
}catch(i){return false
}},iterate:function(k,j){var l;
j=j||0;
(function i(){l=setTimeout(function(){if(k()===false){return
}i()
},j)
})();
return function(){clearTimeout(l)
}
},each:function(o,p,k){if(!o){return
}var n,l=0,m=o.length,j=a.util.isArray(o);
if(k){if(j){for(;
l<m;
l++){n=p.apply(o[l],k);
if(n===false){break
}}}else{for(l in o){n=p.apply(o[l],k);
if(n===false){break
}}}}else{if(j){for(;
l<m;
l++){n=p.call(o[l],l,o[l]);
if(n===false){break
}}}else{for(l in o){n=p.call(o[l],l,o[l]);
if(n===false){break
}}}}return o
},extend:function(m){var l,k,j;
for(l=1;
l<arguments.length;
l++){if((k=arguments[l])!=null){for(j in k){m[j]=k[j]
}}}return m
},on:function(k,j,i){if(k.addEventListener){k.addEventListener(j,i,false)
}else{if(k.attachEvent){k.attachEvent("on"+j,i)
}}},off:function(k,j,i){if(k.removeEventListener){k.removeEventListener(j,i,false)
}else{if(k.detachEvent){k.detachEvent("on"+j,i)
}}},log:function(k,j){if(window.console){var i=window.console[k];
if(typeof i==="function"){i.apply(window.console,j)
}}},warn:function(){a.util.log("warn",arguments)
},info:function(){a.util.log("info",arguments)
},debug:function(){a.util.log("debug",arguments)
},error:function(){a.util.log("error",arguments)
},xhr:function(){try{return new window.XMLHttpRequest()
}catch(j){try{return new window.ActiveXObject("Microsoft.XMLHTTP")
}catch(i){}}},parseJSON:function(i){return !i?null:window.JSON&&window.JSON.parse?window.JSON.parse(i):new Function("return "+i)()
},stringifyJSON:function(k){var n=/[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,l={"\b":"\\b","\t":"\\t","\n":"\\n","\f":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"};
function i(o){return'"'+o.replace(n,function(p){var q=l[p];
return typeof q==="string"?q:"\\u"+("0000"+p.charCodeAt(0).toString(16)).slice(-4)
})+'"'
}function j(o){return o<10?"0"+o:o
}return window.JSON&&window.JSON.stringify?window.JSON.stringify(k):(function m(t,s){var r,q,o,p,w=s[t],u=typeof w;
if(w&&typeof w==="object"&&typeof w.toJSON==="function"){w=w.toJSON(t);
u=typeof w
}switch(u){case"string":return i(w);
case"number":return isFinite(w)?String(w):"null";
case"boolean":return String(w);
case"object":if(!w){return"null"
}switch(Object.prototype.toString.call(w)){case"[object Date]":return isFinite(w.valueOf())?'"'+w.getUTCFullYear()+"-"+j(w.getUTCMonth()+1)+"-"+j(w.getUTCDate())+"T"+j(w.getUTCHours())+":"+j(w.getUTCMinutes())+":"+j(w.getUTCSeconds())+'Z"':"null";
case"[object Array]":o=w.length;
p=[];
for(r=0;
r<o;
r++){p.push(m(r,w)||"null")
}return"["+p.join(",")+"]";
default:p=[];
for(r in w){if(b.call(w,r)){q=m(r,w);
if(q){p.push(i(r)+":"+q)
}}}return"{"+p.join(",")+"}"
}}})("",{"":k})
},checkCORSSupport:function(){if(a.util.browser.msie&&!window.XDomainRequest&&+a.util.browser.version.split(".")[0]<11){return true
}else{if(a.util.browser.opera&&+a.util.browser.version.split(".")<12){return true
}else{if(a.util.trim(navigator.userAgent).slice(0,16)==="KreaTVWebKit/531"){return true
}else{if(a.util.trim(navigator.userAgent).slice(-7).toLowerCase()==="kreatel"){return true
}}}}var i=navigator.userAgent.toLowerCase();
var j=i.indexOf("android")>-1;
if(j){return true
}return false
}};
e=a.util.now();
(function(){var j=navigator.userAgent.toLowerCase(),i=/(chrome)[ \/]([\w.]+)/.exec(j)||/(webkit)[ \/]([\w.]+)/.exec(j)||/(opera)(?:.*version|)[ \/]([\w.]+)/.exec(j)||/(msie) ([\w.]+)/.exec(j)||/(trident)(?:.*? rv:([\w.]+)|)/.exec(j)||j.indexOf("compatible")<0&&/(mozilla)(?:.*? rv:([\w.]+)|)/.exec(j)||[];
a.util.browser[i[1]||""]=true;
a.util.browser.version=i[2]||"0";
if(a.util.browser.trident){a.util.browser.msie=true
}if(a.util.browser.msie||(a.util.browser.mozilla&&+a.util.browser.version.split(".")[0]===1)){a.util.storage=false
}})();
a.util.on(window,"unload",function(i){a.unsubscribe()
});
a.util.on(window,"keypress",function(i){if(i.charCode===27||i.keyCode===27){if(i.preventDefault){i.preventDefault()
}}});
a.util.on(window,"offline",function(){d=true;
if(h.length>0){var j=[].concat(h);
for(var l=0;
l<j.length;
l++){var k=j[l];
k.close();
clearTimeout(k.response.request.id);
if(k.heartbeatTimer){clearTimeout(k.heartbeatTimer)
}}}});
a.util.on(window,"online",function(){if(h.length>0){for(var j=0;
j<h.length;
j++){h[j].init();
h[j].execute()
}}d=false
});
return a
}));
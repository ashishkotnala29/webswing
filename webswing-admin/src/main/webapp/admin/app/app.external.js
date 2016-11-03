(function(define) {
	define("app.external", function f() {
		var module = angular.module('wsExternal', [ 'wsServices' ]);
		var value = {
			onlineUpdatableConfigFields : [ 'swingConfig' ],
			hiddenServerConfigFields : [ 'path', 'icon', 'swingConfig' ],
			getConfigInfo : getConfigInfo,
			showConfigButton : true
		};
		module.value('extValue', value);

		function getConfigInfo(info) {
			var c = info.config;
			var result = {
				"Home Folder" : c.homeDir,
				"Web Folder" : c.webFolder,
				"Security Module" : c.security.module,
			}
			if (c.swingConfig != null) {
				result["Type"] = c.swingConfig.launcherType;
				result["DirectDraw"] = c.swingConfig.directdraw;
				result["Theme"] = c.swingConfig.theme;
				result["Session mode"] = c.swingConfig.sessionMode;
				result["Session timeout"] = c.swingConfig.swingSessionTimeout;
			}
			return result
		}

	});
}(adminConsole.define));
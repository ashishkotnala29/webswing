    "use strict";
    export default function JsLink(util) {
        var module = this;
        module.provides = {
            get: get,
            dispose: dispose
        };

        var cookieName = 'webswingID';
        function get() {
            var id = util.readCookie(cookieName);
            if (id != null) {
                return id;
            } else {
                id = util.GUID();
                util.createCookie(cookieName, id, 1);
                return id;
            }
        }

        function dispose() {
            util.eraseCookie(cookieName);
        }

    }
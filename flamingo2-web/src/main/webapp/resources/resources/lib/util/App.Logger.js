Ext.namespace('App.Logger');
Console = new (function () {

	/**
	 * 문자열 메시지를 Console에 로깅한다.
	 */
	this.log = function (header, output) {
		if (typeof window.console != 'undefined' && typeof console === "object" && console.log) {
			if (App.Util.Lang.is('Object', output)) {
				console.log(header + " => ");
				console.log(output);
			} else {
				console.log(header + " => " + output);
			}
		}
	},

	/**
	 * 문자열 메시지를 Console에 로깅한다.
	 */
	this.log = function (output) {
		if (typeof window.console != 'undefined' && typeof console === "object" && console.log) {
			console.log(output);
		}
	}
});

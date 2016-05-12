function bundle(message) {

    this.message = message;

    this.msg = function (key, args) {
        if(arguments.length > 1) {
            var value = message[key];
            return value.replace(/\{(\d+)\}/g, function (m, i) {
                return args[i];
            });
        } else {
            return message[key];
        }
    };
}

var message;

// 동기 Ajax 호출을 위해서 jQuery를 사용한다.
jQuery.ajax({
    url: '/config/bundle.json?lang=' + config['default.locale'],
    success: function (content) {
        message = new bundle(content);
    },
    async: false
});

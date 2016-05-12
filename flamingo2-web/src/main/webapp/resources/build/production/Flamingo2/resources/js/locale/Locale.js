var head = document.getElementsByTagName('head')[0];
var el = document.createElement("script");
el.type = "text/javascript";
el.src = '/resources/js/locale/ext-locale-' + config['default.locale'] + '.js';
head.appendChild(el);
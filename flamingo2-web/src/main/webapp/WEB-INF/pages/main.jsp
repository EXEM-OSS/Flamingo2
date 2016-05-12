<%@ page import="org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <c:if test="${nocache}">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Expires" content="0">
    </c:if>

    <title><%=ConfigurationHelper.getHelper().get("application.title")%></title>

    <!-- Loading indicator -->
    <script data-pace-options='{ "ajax": false }' src="/resources/lib/pace-1.0.2/pace.js"></script>
    <link href="/resources/lib/pace-1.0.2/themes/orange/pace-theme-center-simple.css" rel="stylesheet"/>

    <!-- Webfont -->
    <link type="text/css" rel="stylesheet" href="/resources/lib/font-awesome-4.3.0/css/font-awesome.min.css"/>
    <link type="text/css" rel="stylesheet" href="/resources/css/nanumgothic.css"/>

    <link type="text/css" rel="stylesheet" href="/resources/css/application.css"/>
    <link type="text/css" rel="stylesheet" href="/resources/css/icons.css"/>
    <link type="text/css" rel="stylesheet" href="/resources/css/chosen.css"/>
    <link type="text/css" rel="stylesheet" href="/resources/css/ace.css"/>
    <link type="text/css" rel="stylesheet" href="/resources/css/node-list.css"/>
    <!--[if IE 9]>
    <link type="text/css" rel="stylesheet" href="/resources/css/ie.css"/>
    <![endif]-->

    <!-- Flamingo Web Configuration -->
    <script type="text/javascript" src="${pageContext.request.contextPath}/config/js.json"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/patch.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/message.js"></script>

    <script type="text/javascript" src="/resources/lib/jquery/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="/resources/lib/bootstrap-3.3.1/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/resources/lib/ace/elements.scroller.js"></script>
    <script type="text/javascript" src="/resources/lib/ace/ace.js"></script>
    <script type="text/javascript" src="/resources/lib/ace/ace-extra.js"></script>
    <script type="text/javascript" src="/resources/lib/ace/ace.sidebar-scroll-1.js"></script>
    <script type="text/javascript" src="/resources/lib/ace/chosen.jquery.js"></script>

    <!-- OpenGraph -->
    <!-- app.json으로 이동하지 마세요 오류납니다.-->
    <script src="${pageContext.request.contextPath}/resources/lib/opengraph/OpenGraph-0.1-SNAPSHOT.js"></script>
    <script src="${pageContext.request.contextPath}/resources/lib/opengraph/lib/jquery-ui-1.11.4/jquery-ui.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/lib/opengraph/lib/contextmenu/jquery.contextMenu-min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/lib/jquery-cookie/jquery.cookie.js"></script>

    <!-- Code Mirror -->
    <script src="${pageContext.request.contextPath}/resources/lib/codemirror-5.1/lib/codemirror.js"></script>
    <script src="${pageContext.request.contextPath}/resources/lib/codemirror-5.1/mode/pig/pig.js"></script>
    <script src="${pageContext.request.contextPath}/resources/lib/codemirror-5.1/mode/sql/sql.js"></script>
    <script src="${pageContext.request.contextPath}/resources/lib/codemirror-5.1/mode/shell/shell.js"></script>
    <script src="${pageContext.request.contextPath}/resources/lib/codemirror-5.1/mode/python/python.js"></script>
    <script src="${pageContext.request.contextPath}/resources/lib/codemirror-5.1/mode/xml/xml.js"></script>


    <!-- Ace Editor -->
    <script src="${pageContext.request.contextPath}/resources/lib/aceeditor/src-noconflict/ace.js"></script>
    <script src="${pageContext.request.contextPath}/resources/lib/aceeditor/src-noconflict/ext-language_tools.js"></script>

    <!--WebSocket for Terminal-->
    <script src="${pageContext.request.contextPath}/resources/lib/socketio/socket.io-1.3.3.js"></script>
    <script src="${pageContext.request.contextPath}/resources/lib/termjs/term.js"></script>
    <script src="${pageContext.request.contextPath}/resources/lib/termjs/Hangul.js"></script>

    <!--WebSocket-->
    <script src="${pageContext.request.contextPath}/resources/lib/websocket/sockjs-1.0.0.js"></script>
    <script src="${pageContext.request.contextPath}/resources/lib/websocket/stomp.js"></script>

    <script type="text/javascript" src="/resources/js/rest.js"></script>
    <script type="text/javascript" src="/resources/js/patch.js"></script>

    <!-- The line below must be kept intact for Sencha Cmd to build your application -->
    <c:choose>
        <c:when test="${mode == 'development'}">
            <script id="microloader" type="text/javascript" src="${pageContext.request.contextPath}/bootstrap.js"></script>
        </c:when>
        <c:otherwise>
            <script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap-min.js"></script>
        </c:otherwise>
    </c:choose>

    <script type="text/javascript">
        SESSION = {};
        SESSION.USERNAME = '${username}';
        SESSION.USERGROUP = '${userGroup}';
        SESSION.NAME = '${name}';
        SESSION.EMAIL = '${email}';
        var LICENSE = {};
        LICENSE.TRIAL = '<c:out value="${isTrial}" />';
        LICENSE.EXPIREDATE = '<c:out value="${expireDate}" />';
        LICENSE.DAYS = '<c:out value="${days}" />';

        function bundle(message) {

            this.message = message;

            this.msg = function (key, args) {
                if (arguments.length > 1) {
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

        $.ajax({
            url: '/config/bundle.json?lang=' + config['default.locale'],
            success: function (content) {
                message = new bundle(content);
            },
            async: false
        });

        var ENGINE = {};
        var WEBSOCKET;
        var MAHOUT = {};
        MAHOUT.JAR = '<%=ConfigurationHelper.getHelper().get("mahout.mapreduce.jar.path")%>';

        var ANKUS = {};
        ANKUS.JAR = '<%=ConfigurationHelper.getHelper().get("flamingo.mapreduce.jar.path")%>';
    </script>

</head>
<body></body>
</html>

<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>

<%@page import="org.apache.commons.io.IOUtils" %>
<%@page import="org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper" %>
<%@page import="java.io.InputStream" %>
<%@page import="java.net.URL" %>
<%@page import="java.net.URLConnection" %>
<%@page import="org.opencloudengine.flamingo2.model.rest.User" %>

<%
    String url = ConfigurationHelper.getHelper().get("rserver.auth.url");
    URLConnection conn = new URL(url).openConnection();
    InputStream in = conn.getInputStream();
    String res = IOUtils.toString(in);
    User user = (User) session.getAttribute("user");
%>

<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="/resources/lib/encrypt/encrypt.min.js"></script>
    <script type="text/javascript">
        function submitRealForm() {
            var payload = '<%=user.getUsername()%>\n<%=user.getPassword()%>';
            var response = '<%=res%>';
            var chunks = response.split(':', 2);
            var exp = chunks[0];
            var mod = chunks[1];

            document.getElementById('package').value = encrypt(payload, exp, mod);
            document.getElementById('clientPath').value = window.location.pathname;
            document.realform.submit();
        }
    </script>
</head>

<form action="<%=ConfigurationHelper.getHelper().get("rserver.signin.url")%>" name="realform" method="POST">
    <input type="hidden" name="persist" id="persist" value="0"/>
    <input type="hidden" name="appUri" value=""/>
    <input type="hidden" name="clientPath" id="clientPath" value=""/>
    <input type="hidden" id="package" name="v" value=""/>
</form>

<script type="text/javascript">
    submitRealForm();
</script>
<body></body>
</html>

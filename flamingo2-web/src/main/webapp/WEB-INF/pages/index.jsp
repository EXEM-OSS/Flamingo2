<%@ page import="org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
    <link type="text/css" rel="stylesheet" href="/resources/lib/bootstrap-3.3.1/css/bootstrap.min.css"/>
    <link type="text/css" rel="stylesheet" href="/resources/css/login.css"/>
    <script type="text/javascript" src="/resources/lib/jquery/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="/resources/lib/bootstrap-3.3.1/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/resources/lib/respond/respond.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            <c:if test="${not empty SUCCESS}">
                $('#myModal').modal('show');
            </c:if>

            <c:if test="${isExpired}">
                $('#licenseExpired').modal({ keyboard: false });
                $('#btn-license').click(function (e) {
                    location.href='/license';
                });
            </c:if>
        });
    </script>
</head>
<body>
<div class="login-title"><h2><%=ConfigurationHelper.getHelper().get("application.title")%></h2></div>
<div class="container">
    <div class="row">
        <div class="col-md-6 col-md-offset-3">
            <div class="panel panel-login">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-xs-6">
                            <a href="#" class="active" id="login-form-link">Login</a>
                        </div>
                        <div class="col-xs-6">
                            <a href="#" id="register-form-link">Registration</a>
                        </div>
                    </div>
                    <hr>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-12">
                            <form id="login-form" action="/j_spring_security_check" method="post" role="form" style="display: block;">
                                <c:if test="${!isExpired}">
                                <div class="form-group">
                                    <input type="text" name="username" id="username" tabindex="1" class="form-control" placeholder="Username" value="" required autofocus>
                                </div>
                                <div class="form-group">
                                    <input type="password" name="password" id="password" tabindex="2" class="form-control" placeholder="Password" required>
                                </div>
                                </c:if>
                                <c:if test="${isExpired}">
                                    <h3 style="color: red">License Expired</h3>
                                    <h5><a href="<%=ConfigurationHelper.getHelper().get("homepage")%>"><%=ConfigurationHelper.getHelper().get("homepage")%></a> You must reissue the license key.</h5>
                                </c:if>
                                <div class="form-group">
                                    <div class="row">
                                        <c:if test="${!isExpired}">
                                        <div id="btn-login" class="col-sm-6 col-sm-offset-3">
                                            <input type="submit" name="login-submit" id="login-submit" tabindex="4" class="form-control btn btn-login" value="Login">
                                        </div>
                                        </c:if>
                                        <c:if test="${isExpired}">
                                        <div id="btn-license" class="col-sm-6 col-sm-offset-3">
                                            <button type="button" id="license" tabindex="4" class="form-control btn btn-login">License</button>
                                        </div>
                                        </c:if>
                                    </div>
                                </div>
                            </form>
                            <form id="register-form" action="/auth/register" method="post" role="form" style="display: none;">
                                <div class="form-group">
                                    <input type="text" name="username" id="username" tabindex="1" class="form-control" placeholder="Username" value="" required>
                                </div>
                                <div class="form-group">
                                    <input type="text" name="name" id="name" tabindex="1" class="form-control" placeholder="Name" value="" required>
                                </div>
                                <div class="form-group">
                                    <input type="email" name="email" id="email" tabindex="1" class="form-control" placeholder="Email" value="" required>
                                </div>
                                <div class="form-group">
                                    <input type="password" name="password" id="password" tabindex="2" class="form-control" placeholder="Password" required>
                                </div>
                                <div class="form-group">
                                    <input type="password" name="confirmPassword" id="confirm-password" tabindex="2" class="form-control" placeholder="Confirm Password" required>
                                </div>
                                <div class="form-group">
                                    <div class="row">
                                        <div class="col-sm-6 col-sm-offset-3">
                                            <input type="submit" name="register-submit" id="register-submit" tabindex="4" class="form-control btn btn-register" value="Regist">
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title" id="myModalLabel">Warning</h5>
            </div>
            <div class="modal-body">
                Invalid username or password
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">OK</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="licenseExpired" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Warning</h5>homepage
            </div>
            <div class="modal-body">
                License Expired.<br><a href="<%=ConfigurationHelper.getHelper().get("homepage")%>"><%=ConfigurationHelper.getHelper().get("homepage")%></a> You must reissue the license key.
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">OK</button>
            </div>
        </div>
    </div>
</div>
<div align="center"><a href="<%=ConfigurationHelper.getHelper().get("homepage")%>" target="_blank" alt="<%=ConfigurationHelper.getHelper().get("organization")%>">Copyright ⓒ 2011 <%=ConfigurationHelper.getHelper().get("organization")%> All rights reserved.</a></div>
<script type="text/javascript">
    $(function () {
        $('#login-form-link').click(function (e) {
            $("#login-form").delay(100).fadeIn(100);
            $("#register-form").fadeOut(100);
            $('#register-form-link').removeClass('active');
            $(this).addClass('active');
            e.preventDefault();
        });
        $('#register-form-link').click(function (e) {
            $("#register-form").delay(100).fadeIn(100);
            $("#login-form").fadeOut(100);
            $('#login-form-link').removeClass('active');
            $(this).addClass('active');
            e.preventDefault();
        });

    });
</script>
</body>
</html>

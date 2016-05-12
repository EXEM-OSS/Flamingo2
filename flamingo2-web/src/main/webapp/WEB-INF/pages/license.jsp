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

    <title><%=ConfigurationHelper.getHelper().get("application.top")%></title>
    <link type="text/css" rel="stylesheet" href="/resources/lib/bootstrap-3.3.1/css/bootstrap.min.css"/>
    <link type="text/css" rel="stylesheet" href="/resources/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />
    <link type="text/css" rel="stylesheet" href="/resources/css/license.css"/>
    <script type="text/javascript" src="/resources/lib/jquery/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="/resources/lib/bootstrap-3.3.1/js/bootstrap.min.js"></script>

</head>
<body>
<div class="flamingo-header">
    <a class="navbar-brand"><%=ConfigurationHelper.getHelper().get("application.title")%></a>
</div>
<div class="page-content">
    <!-- /section:settings.box -->
    <div class="row">
        <div class="col-xs-12">
            <!-- PAGE CONTENT BEGINS -->
            <div class="space-6"></div>

            <div class="row">
                <div class="col-sm-10 col-sm-offset-1">
                    <!-- #section:pages/invoice -->
                    <div class="widget-box transparent">
                        <div class="widget-header widget-header-large">
                            <h3 class="widget-title grey lighter">
                                <i class="ace-icon fa fa-leaf green"></i>
                                License
                            </h3>
                        </div>
                        <div class="widget-box transparent ui-sortable-handle" style="opacity: 1; z-index: 0;">
                            <div class="widget-header">
                                <h4 class="widget-title lighter">Server ID : </h4>
                                <div class="widget-body">
                                    <div class="widget-main padding-6 no-padding-left no-padding-right">
                                        <textarea readonly class="form-control" id="form-field-serverId" style="resize: none;"><c:out value="${serverId}"/></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="widget-box transparent ui-sortable-handle">
                            <div class="widget-header">
                                <h4 class="widget-title lighter">License Key</h4>
                            </div>
                            <div class="widget-body">
                                <div class="widget-main padding-6 no-padding-left no-padding-right">
                                    <textarea class="form-control" id="form-field-license" placeholder="Flamingo License Key를 입력하시오."></textarea>
                                </div>
                            </div>
                        </div>
                        <div id="div-confirm">
                            <button id="btn-confirm" class="btn btn-sm btn-primary">OK</button>
                        </div>
                        <div class="space"></div>
                        <div class="widget-box transparent license-info">
                            <div class="widget-header">
                                <h5 class="widget-title">License Information</h5>
                                <!-- /section:custom/widget-box.toolbar -->
                            </div>
                            <div class="widget-body">
                                <div class="col-sm-6">
                                    <ul class="list-unstyled spaced">
                                        <li>
                                            <i class="ace-icon fa fa-caret-right blue"></i>Registration Date:
                                            <b id="issueDate" class="red"></b>
                                        </li>
                                        <li>
                                            <i class="ace-icon fa fa-caret-right blue"></i>Product:
                                            <b id="PRODUCT_NAME" class="red"></b>
                                        </li>
                                        <li>
                                            <i class="ace-icon fa fa-caret-right blue"></i>License Type:
                                            <b id= "LICENSE_TYPE" class="red"></b>
                                        </li>
                                        <li>
                                            <i class="ace-icon fa fa-caret-right blue"></i>Max Node:
                                            <b id= "MAX_NODE" class="red"></b>
                                        </li>
                                    </ul>
                                </div>
                                <div class="col-sm-6">
                                    <ul class="list-unstyled spaced">
                                        <li>
                                            <i class="ace-icon fa fa-caret-right blue"></i>Expired Date:
                                            <b id= "goodBeforeDate" class="red"></b>
                                        </li>
                                        <li>
                                            <i class="ace-icon fa fa-caret-right blue"></i>Version:
                                            <b id= "PRODUCT_VERSION" class="red"></b>
                                        </li>
                                        <li>
                                            <i class="ace-icon fa fa-caret-right blue"></i>Allow Country:
                                            <b id= "COUNTRY" class="red"></b>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                            <button id="btn-login" class="btn btn-sm btn-success btn-block">Go to Login</button>
                        </div>
                    </div>
                    <!-- /section:pages/invoice -->
                </div>
            </div>

            <!-- PAGE CONTENT ENDS -->
        </div><!-- /.col -->
    </div><!-- /.row -->
</div>
<div class="modal fade" id="license-input-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"></span></button>
                <h5 class="modal-title">Warning</h5>
            </div>
            <div class="modal-body">
                Please, Select License Key
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-sm btn-danger" data-dismiss="modal">OK</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="license-invalid-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"></span></button>
                <h5 class="modal-title">Warning</h5>
            </div>
            <div class="modal-body">
                License Key is invalid.
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-sm btn-danger" data-dismiss="modal">OK</button>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="/resources/js/license.js"></script>
</html>

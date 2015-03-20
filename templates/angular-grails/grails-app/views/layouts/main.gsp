<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Grails"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <asset:stylesheet src="application.css"/>

    <g:layoutHead/>
</head>

<body id="ng-app" ng-app="${pageProperty(name: 'body.ng-app') ?: 'grails'}">

<div class="container-fluid">

    <nav class="navbar navbar-default" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <g:link uri="/" class="navbar-brand">Angular Grails</g:link>
            </div>
            <ul class="nav navbar-nav navbar-left">
                <li><g:link uri="/"><i class="fa fa-home"></i> Home</g:link></li>
                <g:each var="c"
                        in="${grailsApplication.controllerClasses.findAll { it.logicalPropertyName != 'assets' }.sort {
                            it.fullName
                        }}">
                    <li><a href="${c.logicalPropertyName}"><i
                            class="fa fa-database"></i> ${c.logicalPropertyName.capitalize()} List</a></li>
                </g:each>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="https://github.com/craigburke/lazybones-angular-grails"><i class="fa fa-github"></i> Github
                </a></li>
            </ul>
        </div>
    </nav>
</div>

<div class="container-fluid">

    <div class="row">
        <div class="col-md-12">

            <div class="animate-view" ng-view></div>
            <g:layoutBody/>
        </div>
    </div>
</div>

<asset:script type="text/javascript">
    angular.module('grails.constants')
        .constant('rootUrl', '${request.contextPath}')
        .constant('pageSize', ${grailsApplication.config.angular.pageSize})
        .constant('dateFormat', '${grailsApplication.config.angular.dateFormat}');
</asset:script>

<asset:javascript src="application.js"/>
<asset:deferredScripts/>
<g:pageProperty name="page.scripts" default=""/>
</body>
</html>

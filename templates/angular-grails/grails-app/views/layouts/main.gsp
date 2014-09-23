<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">

  		<asset:stylesheet src="application.css"/>

        <asset:script type="text/javascript">
            angular.module('angularGrails.constants', [])
                .constant('rootUrl', '${request.contextPath}')
                .constant('pageSize', ${grailsApplication.config.angular.pageSize})
                .constant('dateFormat', '${grailsApplication.config.angular.dateFormat}');
        </asset:script>

        <asset:javascript src="application.js"/>
        <asset:deferredScripts />
		<g:layoutHead/>
	</head>
	<body ng-app="${pageProperty(name: 'body.ng-app') ?: 'angularGrails'}">

    <div class="container-fluid">

    <nav class="navbar" role="navigation">
        <div class="container-fluid">
        <div class="navbar-header">
            <g:link uri="/" class="navbar-brand">Angular Grails</g:link>
        </div>
        <ul class="nav navbar-nav navbar-left">
            <li><g:link uri="/"><i class="fa fa-home"></i> Home</g:link></li>
            <li><g:link uri="/author"><i class="fa fa-user"></i> Author List</g:link></li>
            <li><g:link uri="/book"><i class="fa fa-book"></i> Book List</g:link></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="https://github.com/craigburke/angular-grails"><i class="fa fa-github"></i> Github</a></li>
        </ul>
        </div>
    </nav>
    </div>

    <div class="container-fluid">

        <div class="row">
            <div class="col-md-12">

            <div ng-view></div>
            <g:layoutBody />
        </div>
      </div>
    </div>
    </div>

    </body>
</html>

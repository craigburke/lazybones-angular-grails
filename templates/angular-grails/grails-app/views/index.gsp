<%@ page import="com.craigburke.angular.Book" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to Grails</title>
	</head>
	<body>
        <div class="col-md-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Application Status</h3>
                </div>
                <div class="panel-body">
                    <ul>
                        <li>App version: <g:meta name="app.version"/></li>
                        <li>Grails version: <g:meta name="app.grails.version"/></li>
                        <li>Groovy version: ${GroovySystem.getVersion()}</li>
                        <li>JVM version: ${System.getProperty('java.version')}</li>
                        <li>Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</li>
                        <li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
                        <li>Domains: ${grailsApplication.domainClasses.size()}</li>
                        <li>Services: ${grailsApplication.serviceClasses.size()}</li>
                        <li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
                    </ul>
                </div>
            </div>

            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Installed Plugins</h3>
                </div>
                <div class="panel-body">
                    <ul>
                        <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
                            <li>${plugin.name} - ${plugin.version}</li>
                        </g:each>
                    </ul>
                </div>
            </div>


    </div>
        <div class="col-md-9">
            <h2>Angular Grails</h2>

            <p class="alert alert-info"><i class="fa fa-info-circle"></i> Look around, try adding and deleting some authors and books to see how it all works</p>

            <p>This is an example app that uses both AngularJs and Grails together while trying to take advantage of what each has to offer.</p>

            <p>This app features are all kinds of supporting directives and services contained within an Angular module named <strong>angularGrails</strong></p>

            <p>It also shows how to use Fields plugin along with the Asset Pipeline Template plugin to render your angular templates.</p>

            <p>See the <a href="https://github.com/craigburke/angular-grails">project README</a> for more information on what's included.</p>

            <h3>Links</h3>
            <ul>
                <li><a href="http://www.craigburke.com/2014/07/24/angular-grails-asset-pipeline.html">AngularJS, Grails and the Asset Pipeline</a></li>
                <li><a href="https://github.com/craigburke/angular-grails">Project on Github</a></li>
            </ul>

            </p>

        </div>

	</body>
</html>

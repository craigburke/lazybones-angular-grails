//= require_self <% def domainList = domainProperties.findAll{ it.isDomainClass }.collect { "${it.type.name - (group + '.')}" } %>
//= require routes
//= require controllers
//= require services <%= domainList.collect { '\n//= require /' + getModulePath(formatModuleName(rootModule + '.' + it)) + '/services' }.join("\n")  %>
//= require_tree /${modulePath}/templates/

'use strict';

angular.module('${fullModuleName}', [
	'${rootModule}.core',
	'${fullModuleName}.controllers', ${domainList ? '\n' : ''}<%= domainList.collect { "\t'${formatModuleName(rootModule + '.' + it)}.services'," }.join('\n') %>
	'${fullModuleName}.services'
]);
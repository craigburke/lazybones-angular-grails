//= require_self <% def domainList = domainProperties.findAll{ it.domainClass }.collect { "${it.type.name - (group + '.')}" } %>
//= require controllers
//= require services <%= domainList.collect { '\n//= require /' + getModulePath(formatModuleName(rootModule + '.' + it)) + '/services' }.join("\n")  %>
//= require_tree /${modulePath}/templates/

'use strict';
<% def generateResolveProperty = { item -> """
				${item[0].toLowerCase() + item.substring(1)}List: function(${item}Resource) {
					return ${item}Resource.list();
				}	
"""
}
%>
angular.module('${fullModuleName}', [
	'grails', 
	'${fullModuleName}.controllers', ${domainList ? NEWLINE : ''}<%= domainList.collect { "${TAB}'${formatModuleName(rootModule + '.' + it)}.services'," }.join(NEWLINE) %>
	'${fullModuleName}.services'
])
.value('defaultCrudResource', '${defaultResource}')
.config(function(${DOLLAR_SIGN}routeProvider) {
	${DOLLAR_SIGN}routeProvider
        .when('/', {
            controller: 'ListCtrl as ctrl',
            templateUrl: 'list.html',
            resolve: {
                ${moduleName}List: function(${DOLLAR_SIGN}route, ${defaultResource}) {
                    var params = ${DOLLAR_SIGN}route.current.params;
                    return ${defaultResource}.list(params);
                }<%= (domainList ? ',' : '' ) + domainList.collect{ generateResolveProperty(it) }.join(', ') %> 
            }
        })
        .when('/create', {
            controller: 'CreateEditCtrl as ctrl',
            templateUrl: 'create-edit.html',
            resolve: {
                ${moduleName}: function(${defaultResource}) {
                    return ${defaultResource}.create();
                }<%= (domainList ? ',' : '' ) + domainList.collect{ generateResolveProperty(it) }.join(', ') %> 
            }
        })
        .when('/edit/:id', {
            controller: 'CreateEditCtrl as ctrl',
            templateUrl: 'create-edit.html',
            resolve: {
                ${moduleName}: function(${DOLLAR_SIGN}route, ${defaultResource}) {
                    var id = ${DOLLAR_SIGN}route.current.params.id;
                    return ${defaultResource}.get(id);
                }<%= (domainList ? ',' : '' ) + domainList.collect{ generateResolveProperty(it) }.join(', ') %> 
            }
        })
        .when('/show/:id', {
            controller: 'ShowCtrl as ctrl',
            templateUrl: 'show.html',
            resolve: {
                ${moduleName}: function(${DOLLAR_SIGN}route, ${defaultResource}) {
                    var id = ${DOLLAR_SIGN}route.current.params.id;
                    return ${defaultResource}.get(id);
                }
            }
        })
        .otherwise({redirectTo: '/'});
});

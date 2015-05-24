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
	'${fullModuleName}.controllers', ${domainList ? '\n' : ''}<%= domainList.collect { "\t'${formatModuleName(rootModule + '.' + it)}.services'," }.join('\n') %>
	'${fullModuleName}.services'
])
.value('defaultCrudResource', '${angularResource}')
.config(function(\$routeProvider) {
	\$routeProvider
        .when('/', {
            controller: 'ListCtrl as ctrl',
            templateUrl: 'list.html',
            resolve: {
                ${moduleName}List: function(\$route, ${angularResource}) {
                    var params = \$route.current.params;
                    return ${angularResource}.list(params);
                }<%= (domainList ? ',' : '' ) + domainList.collect{ generateResolveProperty(it) }.join(', ') %> 
            }
        })
        .when('/create', {
            controller: 'CreateEditCtrl as ctrl',
            templateUrl: 'create-edit.html',
            resolve: {
                ${moduleName}: function(${angularResource}) {
                    return ${angularResource}.create();
                }<%= (domainList ? ',' : '' ) + domainList.collect{ generateResolveProperty(it) }.join(', ') %> 
            }
        })
        .when('/edit/:id', {
            controller: 'CreateEditCtrl as ctrl',
            templateUrl: 'create-edit.html',
            resolve: {
                ${moduleName}: function(\$route, ${angularResource}) {
                    var id = \$route.current.params.id;
                    return ${angularResource}.get(id);
                }<%= (domainList ? ',' : '' ) + domainList.collect{ generateResolveProperty(it) }.join(', ') %> 
            }
        })
        .when('/show/:id', {
            controller: 'ShowCtrl as ctrl',
            templateUrl: 'show.html',
            resolve: {
                ${moduleName}: function(\$route, ${angularResource}) {
                    var id = \$route.current.params.id;
                    return ${angularResource}.get(id);
                }
            }
        })
        .otherwise({redirectTo: '/'});
});

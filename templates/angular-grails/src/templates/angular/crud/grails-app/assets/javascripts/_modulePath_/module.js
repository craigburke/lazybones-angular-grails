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
.config(function(\$stateProvider, \$urlRouterProvider) {
    \$urlRouterProvider.when('/${domainClassNameLowerCase}/', '/${domainClassNameLowerCase}/list');
	
	\$stateProvider
		.state('${domainClassNameLowerCase}', {
			abstract:true,
			url:'/${domainClassNameLowerCase}',
			template: '<div ui-view></div>'
		})
		.state('${domainClassNameLowerCase}.list', {
			url: '/list',
			controller:'${domainClassName}ListCtrl as ctrl',
			templateUrl:'/${modulePath}/list.html',
            resolve: {
                ${moduleName}List: function(\$stateParams, ${angularResource}) {
                    return ${angularResource}.list(\$stateParams);
                }
				<%= (domainList ? ',' : '' ) + domainList.collect{ generateResolveProperty(it) }.join(', ') %> 
            }
		})
		.state('${domainClassNameLowerCase}.create', {
			url: '/create',
			controller:'${domainClassName}CreateEditCtrl as ctrl',
			templateUrl:'/${modulePath}/create-edit.html',
            resolve: {
				${moduleName}: function(${angularResource}) {
                return ${angularResource}.create();
            	}
				<%= (domainList ? ',' : '' ) + domainList.collect{ generateResolveProperty(it) }.join(', ') %>
			}
		})
		.state('${domainClassNameLowerCase}.edit', {
			url: '/edit/:id',
			controller:'${domainClassName}CreateEditCtrl as ctrl',
			templateUrl:'/${modulePath}/create-edit.html',
            resolve: { 
				${moduleName}: function(\$stateParams, ${angularResource}) {
				return ${angularResource}.get(\$stateParams.id);
            	}
				<%= (domainList ? ',' : '' ) + domainList.collect{ generateResolveProperty(it) }.join(', ') %>
			}
		})
		.state('${domainClassNameLowerCase}.show', {
			url: '/show/:id',
			controller:'${domainClassName}ShowCtrl as ctrl',
			templateUrl:'/${modulePath}/show.html',
            resolve: { 
				${moduleName}: function(\$stateParams, ${angularResource}) {
					return ${angularResource}.get(\$stateParams.id);
            	}
				<%= (domainList ? ',' : '' ) + domainList.collect{ generateResolveProperty(it) }.join(', ') %>
			}
		})
		
});

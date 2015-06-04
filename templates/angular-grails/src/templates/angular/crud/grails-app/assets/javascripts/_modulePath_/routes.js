'use strict';<% def domainList = domainProperties.findAll{ it.isDomainClass }.collect { "${it.type.name - (group + '.')}" } %>
<% def generateResolveProperty = { item -> """
				${item[0].toLowerCase() + item.substring(1)}List: function(${item}Service) {
					return ${item}Service.list();
				}	
"""
}
%>
angular.module('${fullModuleName}')
.config(function(\$stateProvider, \$urlRouterProvider) {
	\$urlRouterProvider.when('/${domainClassNameLowerCase}/', '/${domainClassNameLowerCase}/list');

	\$stateProvider
		.state('${domainClassNameLowerCase}', {
			abstract:true,
			url:'/${domainClassNameLowerCase}',
			template: '<div ui-view></div>'
		})
		.state('${domainClassNameLowerCase}.list', {
			url:'/list',
			controller:'${domainClassName}ListCtrl as ctrl',
			templateUrl:'/${modulePath}/list.html',
            resolve: {
                ${moduleName}List: function(\$stateParams, ${domainClassName}Service) {
                    return ${domainClassName}Service.list(\$stateParams);
                }<%= (domainList ? ',' : '' ) + domainList.collect{ generateResolveProperty(it) }.join(', ') %> 
            }
		})
		.state('${domainClassNameLowerCase}.create', {
			url:'/create',
			controller:'${domainClassName}CreateEditCtrl as ctrl',
			templateUrl:'/${modulePath}/create-edit.html',
            resolve: {
				${moduleName}: function(${domainClassName}Service) {
                	return ${domainClassName}Service.create();
            	}<%= (domainList ? ',' : '' ) + domainList.collect{ generateResolveProperty(it) }.join(', ') %>
			}
		})
		.state('${domainClassNameLowerCase}.edit', {
			url:'/edit/:id',
			controller:'${domainClassName}CreateEditCtrl as ctrl',
			templateUrl:'/${modulePath}/create-edit.html',
            resolve: { 
				${moduleName}: function(\$stateParams, ${domainClassName}Service) {
					return ${domainClassName}Service.get(\$stateParams.id);
            	}<%= (domainList ? ',' : '' ) + domainList.collect{ generateResolveProperty(it) }.join(', ') %>
			}
		})
		.state('${domainClassNameLowerCase}.show', {
			url:'/show/:id',
			controller:'${domainClassName}ShowCtrl as ctrl',
			templateUrl:'/${modulePath}/show.html',
            resolve: { 
				${moduleName}: function(\$stateParams, ${domainClassName}Service) {
					return ${domainClassName}Service.get(\$stateParams.id);
            	}<%= (domainList ? ',' : '' ) + domainList.collect{ generateResolveProperty(it) }.join(', ') %>
			}
		});
});
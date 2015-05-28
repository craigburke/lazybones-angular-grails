//= require_self
//= require /grails/directives/templates/crud-breadcrumbs

'use strict';

function crudBreadcrumbs($injector, defaultCrudResource) {
    return {
        restrict: 'EA',
        replace: true,
        scope: {
            crudBreadcrumbs: '@',
			urlPrefix: '@'
        },
		link: function($scope) {
			$scope.urlPrefix = $scope.urlPrefix || '/';
            var defaultResource = $injector.get(defaultCrudResource);
			$scope.resourceName = defaultResource.getName();
		},
        templateUrl: '/grails/directives/crud-breadcrumbs.html'
    }
}

angular.module('grails.directives.crudBreadcrumbs', ['grails.services'])
    .directive('crudBreadcrumbs', crudBreadcrumbs);

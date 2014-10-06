//= require_self
//= require_tree /grails/templates/directives/crud-breadcrumbs

'use strict';

function crudBreadcrumbs($injector, defaultCrudResource) {
    return {
        restrict: 'EA',
        replace: true,
        scope: {
            crudBreadcrumbs: '@'
        },
		link: function($scope) {
            var defaultResource = $injector.get(defaultCrudResource);
			$scope.resourceName = defaultResource.getName();
		},
        templateUrl: 'crud-breadcrumbs.html'
    }
}

angular.module('grails.directives.crudBreadcrumbs', ['grails.services'])
    .directive('crudBreadcrumbs', crudBreadcrumbs);

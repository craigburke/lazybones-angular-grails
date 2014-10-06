//= require_self
//= require_tree /grails/templates/directives

'use strict';

function crudNav($injector, defaultCrudResource) {
    return {
        restrict: 'EA',
        replace: true,
        scope: {
            crudNav: '@'
        },
		link: function($scope) {
            var defaultResource = $injector.get(defaultCrudResource);
			$scope.resourceName = defaultResource.getName();
		},
        templateUrl: 'nav.html'
    }
}

angular.module('grails.directives.nav', ['grails.services'])
    .directive('crudNav', crudNav);

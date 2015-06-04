'use strict';

function menuItem(\$state) {
    return {
        restrict: 'EA',
        replace: false,
        scope: {
            menuItem: '='
        },
        link: function(\$scope, \$element, \$attrs) {

			\$scope.\$on('\$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
				toggleMenuState(toState.name);
			});
			
			var toggleMenuState = function(currentState) {
				angular.forEach(\$scope.menuItem, function(value, key) {
					var stateMatched = false;
					if (key.slice(-1) == '*') {
						var searchString = key.slice(0, -1);
						stateMatched = (currentState.lastIndexOf(searchString) == 0);
					}
					else {
						stateMatched = (currentState === key)
					}
				
					if (stateMatched) {
						\$element.addClass(value);
					}
					else {
						\$element.removeClass(value);
					}
				});
			}
						
        }
    }
}

angular.module('${baseModule}.core.directives.menu', ['ui.router'])
    .directive('menuItem', menuItem);

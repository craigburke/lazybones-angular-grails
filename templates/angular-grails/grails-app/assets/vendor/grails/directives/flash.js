//= require_self
//= require_tree /grails/templates/directives

'use strict';

function flashMessage(FlashService) {
    return {
        restrict: 'EA',
        replace: true,
        scope: {},
        link: function($scope) {
            $scope.getMessageClass = function(type) {
                switch(type) {
                    case FlashService.TYPE.ERROR:
                        return "alert-danger";
                    case FlashService.TYPE.WARN:
                        return "alert-warning";
                    default:
                        return "alert-" + type;
                }
            };

            $scope.isList = function(message) {
                return (message instanceof Array);
            };

            var loadMessage = function() {
				$scope.flash = FlashService.getMessage();
            };
			
		    $scope.$on('$destroy', function () {
				FlashService.clear();
			});

            $scope.$on('flash:messageChange', loadMessage);
			
			loadMessage();
        },
        templateUrl: 'flash-message.html'
    }
}

angular.module('grails.directives.flash', ['grails.services'])
    .directive('flashMessage', flashMessage);

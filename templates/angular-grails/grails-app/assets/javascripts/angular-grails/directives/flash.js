//= require_self
//= require_tree /templates/angular-grails/directives/flash

'use strict';

function flashMessage(FlashService) {
    return {
        restrict: 'EA',
        replace: true,
        scope: {},
        link: function($scope) {
            $scope.getMessageClass = function(type) {
                switch(type) {
                    case FlashService.TYPES.ERROR:
                        return "alert-danger";
                    case FlashService.TYPES.WARN:
                        return "alert-warning";
                    default:
                        return "alert-" + type;
                }
            };

            $scope.isList = function(message) {
                return (message instanceof Array);
            };

            $scope.close = function() {
                FlashService.clear();
            };

            var loadMessage = function() {
                $scope.flash = FlashService.get();
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

angular.module('angularGrails.directives.flash', ['angularGrails.services'])
    .directive('flashMessage', flashMessage);

//= require_self
//= require_tree /templates/grails/directives/buttons

'use strict';

function crudButton($location, $injector, defaultCrudResource, FlashService) {
    return {
        restrict: 'EA',
        replace: true,
        scope: {
            crudButton: '@',
            item: '=',
            isDisabled: '=',
            afterAction: '&'
        },
        link: function ($scope) {

            var defaultResource = $injector.get(defaultCrudResource);

            var createFn = function () {
                $location.path("/create");
                if ($scope.afterAction) {
                    $scope.afterAction();
                }
            };

            var editFn = function () {
                $location.path("/edit/" + $scope.item.id);
                if ($scope.afterAction) {
                    $scope.afterAction();
                }
            };

            var saveFn = function () {
                var errorFunction = function (data) {
                    var messages = [];
                    angular.forEach(data.data.errors, function (error) {
                        messages.push(error.message);
                    });

                    FlashService.error(messages);
                };

                if ($scope.item.id) {
                    defaultResource.update($scope.item,
                        function (response) {
                            $location.path('/show/' + response.id);
                            if ($scope.afterAction) {
                                $scope.afterAction();
                            }
                            $scope.$on('$destroy', function () {
                                FlashService.success(defaultResource.getName() + " was updated");
                            });
                        },
                        errorFunction)
                }
                else {
                    defaultResource.save($scope.item,
                        function (response) {
                            $location.path('/show/' + response.id);
                            if ($scope.afterAction) {
                                $scope.afterAction();
                            }
                            $scope.$on('$destroy', function () {
                                FlashService.success(defaultResource.getName() + " was saved");
                            });
                        },
                        errorFunction)
                }
            };

            var deleteFn = function () {
                var successFn = function () {
                    FlashService.success(defaultResource.getName() + ' was successfully deleted');
                    $location.path('/');
                    if ($scope.afterAction) {
                        $scope.afterAction();
                    }
                };

                var errorFn = function () {
                    FlashService.error("Couldn't delete " + defaultResource.getName());
                };

                defaultResource.delete($scope.item.id, successFn, errorFn);
            };

            $scope.onClick = function () {
                switch ($scope.crudButton) {
                    case "create" :
                        createFn();
                        break;
                    case "edit" :
                        editFn();
                        break;
                    case "delete" :
                        deleteFn();
                        break;
                    case "save" :
                        saveFn();
                        break;
                }
            }
        },
        templateUrl: function (element, attrs) {
            switch (attrs.crudButton) {
                case "create":
                    return "create-button.html";
                case "edit":
                    return "edit-button.html";
                case "delete":
                    return "delete-button.html";
                case "save":
                    return "save-button.html";
                case "cancel":
                    return "cancel-button.html";
            }

        }
    }
}

angular.module('grails.directives.buttons', ['grails.services'])
    .directive('crudButton', crudButton);


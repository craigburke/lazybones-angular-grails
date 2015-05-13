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
							var message = defaultResource.getName() + " was successfully updated";
                            FlashService.success(message, {routeChange: true});
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
							var message = defaultResource.getName() + " was successfully created";
                            FlashService.success(message, {routeChange: true});
                        },
                        errorFunction)
                }
            };

            var deleteFn = function () {
                var successFn = function () {
					var routeChange = ($location.path() !== '/')
					
					if ($scope.afterAction) {
                        $scope.afterAction();
                    }
					if (routeChange) {
						$location.path('/');
					}

					var message = defaultResource.getName() + ' was successfully deleted';
                    FlashService.success(message, {routeChange: routeChange});					
                };

                var errorFn = function () {
					var message = "Couldn't delete " + defaultResource.getName();
                    FlashService.error(message);
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
            }

        }
    }
}

angular.module('grails.directives.buttons', ['grails.services'])
    .directive('crudButton', crudButton);


//= require_self
//= require_tree /grails/directives/templates/buttons

'use strict';

function crudButton($location, $injector, defaultCrudResource, FlashService) {
    return {
        restrict: 'EA',
        replace: true,
        scope: {
            crudButton: '@',
            item: '=',
			urlPrefix: '@',
            isDisabled: '=',
            afterAction: '&'
        },
        link: function ($scope) {
			$scope.urlPrefix = $scope.urlPrefix || '/';
            var defaultResource = $injector.get(defaultCrudResource);

            var createFn = function () {
                $location.path($scope.urlPrefix + "create");
                if ($scope.afterAction) {
                    $scope.afterAction();
                }
            };

            var editFn = function () {
                $location.path($scope.urlPrefix + "edit/" + $scope.item.id);
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
                            $location.path($scope.urlPrefix + 'show/' + response.id);
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
                            $location.path($scope.urlPrefix + 'show/' + response.id);
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
					var routeChange = ($location.path() !== $scope.urlPrefix)
					
					if ($scope.afterAction) {
                        $scope.afterAction();
                    }
					if (routeChange) {
						$location.path($scope.urlPrefix);
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
                    return "/grails/directives/buttons/create-button.html";
                case "edit":
                    return "/grails/directives/buttons/edit-button.html";
                case "delete":
                    return "/grails/directives/buttons/delete-button.html";
                case "save":
                    return "/grails/directives/buttons/save-button.html";
            }

        }
    }
}

angular.module('grails.directives.buttons', ['grails.services'])
    .directive('crudButton', crudButton);


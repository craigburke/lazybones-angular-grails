//= require_self
//= require_tree /grails/directives/templates/buttons

'use strict';

function crudButton($state, FlashService) {
    return {
        restrict: 'EA',
        replace: true,
        scope: {
			label: '@',
            crudButton: '@',
            item: '=',
            isDisabled: '=',
            afterAction: '&'
        },
        link: function($scope) {
            var createFn = function () {
				$state.go('^.create');
                if ($scope.afterAction) {
                    $scope.afterAction();
                }
            };

            var editFn = function () {
				$state.go('^.edit', {id: $scope.item.id});
                if ($scope.afterAction) {
                    $scope.afterAction();
                }
            };

            var saveFn = function () {
                var errorFunction = function(data) {
                    var messages = [];
                    angular.forEach(data.data.errors, function (error) {
                        messages.push(error.message);
                    });

                    FlashService.error(messages);
                };

                $scope.item.save().then(function(item) {
					$state.go('^.show', {id: item.id});
					if ($scope.afterAction) {
                    	$scope.afterAction();
					}
					var message = 'Item was successfully updated';
					FlashService.success(message, {routeChange: true});
                 },errorFunction);
            };

            var deleteFn = function () {
                var successFn = function() {
					var routeChange = (!$state.current.name.endsWith('.list'));
					
					if ($scope.afterAction) {
                		$scope.afterAction();
                	}
					if (routeChange) {
						$state.go('^.list');
					}

					var message = 'Item was successfully deleted';
                	FlashService.success(message, {routeChange: routeChange});					
				};

				var errorFn = function () {
					var message = "Couldn't delete item";
                	FlashService.error(message);
				};

				$scope.item.remove().then(successFn, errorFn);
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


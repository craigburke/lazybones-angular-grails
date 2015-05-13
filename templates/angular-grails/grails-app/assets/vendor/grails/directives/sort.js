//= require_self
//= require_tree /grails/templates/directives

'use strict';

/* @ngInject */
function SortHeaderController($scope) {
    $scope.sort = {sort: undefined, order: undefined};

    this.setSort = function(sort) {
        $scope.sort = sort;
    };

    this.getSort = function() {
        return $scope.sort;
    };
}

function sortHeader() {
    return {
        restrict: 'EA',
        replace: false,
        require: 'ngModel',
        scope: {
            onSort: '&'
        },
        link: function($scope, $element, $attrs, ngModel) {
            $scope.$watch('sort', function() {

                ngModel.$setViewValue($scope.sort);
                if ($scope.onSort && $scope.sort.sort) {
                    $scope.onSort();
                }
            });
        },
        controller: 'SortHeaderController'
    }
}

function sortableColumn() {
    return {
        restrict: 'EA',
        replace: true,
        scope: {
            title: '@',
            property: '@'
        },
        require: '^sortHeader',
        link: function($scope, $element, $attrs, sortHeader) {
            $scope.order = "desc";

            $scope.isActive = function() {
                return (sortHeader.getSort().sort === $scope.property);
            };

            $scope.sort = function() {
                $scope.order = ($scope.order == "asc") ? "desc" : "asc";
                sortHeader.setSort({sort: $scope.property, order: $scope.order});
            }
        },
        templateUrl: 'sortable-column.html'
    }
}

angular.module('grails.directives.sort', [])
    .controller('SortHeaderController', SortHeaderController)
    .directive('sortHeader', sortHeader)
    .directive('sortableColumn', sortableColumn);
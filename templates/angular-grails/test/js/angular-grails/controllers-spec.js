describe('angularGrails Controllers: ', function() {

    var item = {'foo': 'bar', 'count': 100};

    beforeEach(module('angularGrails.controllers'));

    describe('DefaultShowCtrl', function() {
        var ctrl, scope;

        beforeEach(module(function($provide) {
            $provide.value('item', item);
        }));

        beforeEach(inject(
            function ($controller, $rootScope) {
                scope = $rootScope.$new();
                ctrl = $controller('DefaultShowCtrl', { $scope: scope });
            }
        ));

        it('should have the item on the scope', function() {
            expect(ctrl.item).toEqual(item);
        });
    });

    describe('DefaultCreateEditCtrl', function() {
        var ctrl, scope;

        beforeEach(module(function($provide) {
            $provide.value('item', item);
        }));

        beforeEach(inject(
            function ($controller, $rootScope) {
                scope = $rootScope.$new();
                ctrl = $controller('DefaultCreateEditCtrl', { $scope: scope });
            }
        ));

        it('should have the item on the scope', function() {
            expect(ctrl.item).toEqual(item);
        });
    });

    describe('DefaultListCtrl', function() {
        var ctrl, $scope, deferred;

        var items = [
            {id: 1, name: 'Item1'},
            {id: 2, name: 'Item2'}
        ];

        var items2 = [
            {id: 3, name: 'Item3'},
            {id: 4, name: 'Item4'}
        ];

        var pageSize = 25;

        beforeEach(module(function($provide) {

            var mockCrudService = {
                list: function() {
                    deferred.resolve(items2);
                    return deferred.promise;
                }
            };

            $provide.value('defaultCrudResource', 'CrudService');
            $provide.value('CrudService', mockCrudService);
            $provide.value('items', items);
            $provide.value('pageSize', pageSize);
        }));

        beforeEach(inject(
            function ($controller, $rootScope, $q) {
                deferred = $q.defer();
                $scope = $rootScope.$new();
                ctrl = $controller('DefaultListCtrl', { $scope: $scope });
            }
        ));

        it('should have the items and default values set on the scope', function() {
            expect(ctrl.pageSize).toEqual(pageSize);
            expect(ctrl.items).toEqual(items);
            expect(ctrl.page).toEqual(1);
        });

        it('reload should reset page', function() {
            ctrl.page = 2;
            ctrl.reload();
            $scope.$digest();

            expect(ctrl.page).toEqual(1);
            expect(ctrl.items).toEqual(items2);
        });

    });


});

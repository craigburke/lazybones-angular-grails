describe('${resourceName} Controllers: ', function() {

    var item = {'foo': 'bar', 'count': 100};

    beforeEach(module('${fullModuleName}.controllers'));

    describe('ShowCtrl: ', function() {
        var ctrl, scope;

        beforeEach(module(function(\$provide) {
            \$provide.value('${moduleName}', item);
        }));

        beforeEach(inject(
            function (\$controller, \$rootScope) {
                scope = \$rootScope.\$new();
                ctrl = \$controller('ShowCtrl', { \$scope: scope });
            }
        ));

        it('should have the ${moduleName} on the scope', function() {
            expect(ctrl.${moduleName}).toEqual(item);
        });
    });

    describe('CreateEditCtrl: ', function() {
        var ctrl, scope;

        beforeEach(module(function(\$provide) {
            \$provide.value('${moduleName}', item);
<%= domainProperties.take(4).findAll{ it.domainClass }.collect { "\t\t\t\$provide.value('${it.name}List', []);" }.join('\n') %>
        }));

        beforeEach(inject(
            function (\$controller, \$rootScope) {
                scope = \$rootScope.\$new();
                ctrl = \$controller('CreateEditCtrl', { \$scope: scope });
            }
        ));

        it('should have the ${moduleName} on the scope', function() {
            expect(ctrl.${moduleName}).toEqual(item);
        });
    });

    describe('ListCtrl: ', function() {
        var ctrl, scope, deferred;

        var items = [
            {id: 1, name: 'Item1'},
            {id: 2, name: 'Item2'}
        ];

        var items2 = [
            {id: 3, name: 'Item3'},
            {id: 4, name: 'Item4'}
        ];

        var PAGE_SIZE = 25;

        beforeEach(module(function(\$provide) {

            var mockCrudService = {
                list: function() {
                    deferred.resolve(items2);
                    return deferred.promise;
                }
            };

            \$provide.value('${resourceName}Resource', mockCrudService);
            \$provide.value('${moduleName}List', items);
<%= domainProperties.take(4).findAll{ it.domainClass }.collect { "\t\t\t\$provide.value('${it.name}List', []);" }.join('\n') %>
            \$provide.value('pageSize', PAGE_SIZE);
        }));

        beforeEach(inject(
            function (\$controller, \$rootScope, \$q) {
                deferred = \$q.defer();
                scope = \$rootScope.\$new();
                ctrl = \$controller('ListCtrl', { \$scope: scope });
            }
        ));

        it('should have the ${moduleName}List and default values set on the scope', function() {
            expect(ctrl.pageSize).toEqual(PAGE_SIZE);
            expect(ctrl.${moduleName}List).toEqual(items);
            expect(ctrl.page).toEqual(1);
        });

        it('reload should reset page', function() {
            ctrl.page = 2;
            ctrl.reload();
            scope.\$digest();

            expect(ctrl.page).toEqual(1);
            expect(ctrl.${moduleName}List).toEqual(items2);
        });

    });


});
describe('${resourceName} Controllers: ', function() {

    var item = {'foo': 'bar', 'count': 100};

    beforeEach(module('${fullModuleName}.controllers'));

    describe('ShowCtrl: ', function() {
        var ctrl, scope;

        beforeEach(module(function(${DOLLAR_SIGN}provide) {
            ${DOLLAR_SIGN}provide.value('${moduleName}', item);
        }));

        beforeEach(inject(
            function (${DOLLAR_SIGN}controller, ${DOLLAR_SIGN}rootScope) {
                scope = ${DOLLAR_SIGN}rootScope.${DOLLAR_SIGN}new();
                ctrl = ${DOLLAR_SIGN}controller('ShowCtrl', { ${DOLLAR_SIGN}scope: scope });
            }
        ));

        it('should have the ${moduleName} on the scope', function() {
            expect(ctrl.${moduleName}).toEqual(item);
        });
    });

    describe('CreateEditCtrl: ', function() {
        var ctrl, scope;

        beforeEach(module(function(${DOLLAR_SIGN}provide) {
            ${DOLLAR_SIGN}provide.value('${moduleName}', item);
<%= domainProperties.take(4).findAll{ it.domainClass }.collect { "${TAB*3}${DOLLAR_SIGN}provide.value('${it.name}List', []);" }.join(NEWLINE) %>
        }));

        beforeEach(inject(
            function (${DOLLAR_SIGN}controller, ${DOLLAR_SIGN}rootScope) {
                scope = ${DOLLAR_SIGN}rootScope.${DOLLAR_SIGN}new();
                ctrl = ${DOLLAR_SIGN}controller('CreateEditCtrl', { ${DOLLAR_SIGN}scope: scope });
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

        beforeEach(module(function(${DOLLAR_SIGN}provide) {

            var mockCrudService = {
                list: function() {
                    deferred.resolve(items2);
                    return deferred.promise;
                }
            };

            ${DOLLAR_SIGN}provide.value('${resourceName}Resource', mockCrudService);
            ${DOLLAR_SIGN}provide.value('${moduleName}List', items);
<%= domainProperties.take(4).findAll{ it.domainClass }.collect { "${TAB*3}${DOLLAR_SIGN}provide.value('${it.name}List', []);" }.join(NEWLINE) %>
            ${DOLLAR_SIGN}provide.value('pageSize', PAGE_SIZE);
        }));

        beforeEach(inject(
            function (${DOLLAR_SIGN}controller, ${DOLLAR_SIGN}rootScope, ${DOLLAR_SIGN}q) {
                deferred = ${DOLLAR_SIGN}q.defer();
                scope = ${DOLLAR_SIGN}rootScope.${DOLLAR_SIGN}new();
                ctrl = ${DOLLAR_SIGN}controller('ListCtrl', { ${DOLLAR_SIGN}scope: scope });
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
            scope.${DOLLAR_SIGN}digest();

            expect(ctrl.page).toEqual(1);
            expect(ctrl.${moduleName}List).toEqual(items2);
        });

    });


});
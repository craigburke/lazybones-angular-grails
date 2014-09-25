describe('grails sort: ', function() {

    var $rootScope, $compile, $httpBackend;
    var onSort = jasmine.createSpy();

    beforeEach(module('grails.directives.sort'));

    beforeEach(inject(function (_$rootScope_, _$compile_, _$httpBackend_) {
        $rootScope = _$rootScope_;
        $compile = _$compile_;
        $httpBackend = _$httpBackend_;
    }));

    describe('sortHeader directive: ', function () {
        var headerController;
        var headerScope;

        beforeEach(function () {
            $httpBackend.expectGET('sortable-column.html').respond("<th>{{title}}</th>");

            var scope = $rootScope.$new();
            scope.onSort = onSort;
            var element = $compile("<tr sort-header ng-model='sort' on-sort='onSort()'> \
                <th sortable-column title='Id' property='id'></th> \
            </tr>")(scope);

            scope.$digest();
            $httpBackend.flush();

            headerScope = element.isolateScope('sortHeader');
            headerController = element.controller('sortHeader');
        });

        it('should be able to set sort and have callback be fired', function () {
            var currentSort = headerController.getSort();

            expect(currentSort.sort).toBeUndefined();
            expect(currentSort.order).toBeUndefined();

            headerController.setSort({sort: 'id', order: 'asc'});
            headerScope.$digest();

            expect(onSort.calls.count()).toBe(1);
        });


    });

});
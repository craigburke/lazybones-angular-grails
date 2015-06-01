describe('grails CrudResourceFactory: ', function() {
    var CrudServiceFactory;

    beforeEach(module('grails.services.crud'));
    beforeEach(module(function($provide) {
        $provide.value('rootUrl', '/');
    }));
    beforeEach(inject(function(_CrudServiceFactory_) {
        CrudServiceFactory = _CrudServiceFactory_;
    }));

    it('should be able to create multiple CrudResource objects', function () {
        var crudService1 = CrudServiceFactory('/api/foo1');
        var crudService2 = CrudServiceFactory('/api/foo1');

        expect(crudService1).not.toBe(crudService2);
    });

    describe('list method ', function() {
        var itemList = [
            {id: 1, name: 'Foo1'},
            {id: 2, name: 'Foo2'}
        ];
        var $httpBackend, listResponse;

        beforeEach(inject(function(_$httpBackend_) {
            $httpBackend = _$httpBackend_;
            $httpBackend.expectGET('/foo/api').respond(itemList, {'X-Item-Range': '1-2/10'});
        }));

        beforeEach(function(done) {
            var crudService = CrudServiceFactory('/foo/api');

            crudService.list().then(function(response) {
                listResponse = response;
                done();
            });

            $httpBackend.flush();
        });

        it('should return the items and set the totalCount correctly', function() {
            var totalCount = listResponse.getTotalCount();

            expect(totalCount).toEqual(10);
            expect(listResponse.length).toEqual(2);

            expect(listResponse[0].id).toEqual(itemList[0].id);
            expect(listResponse[0].name).toEqual(itemList[0].name);
        });
    });


    describe('get method ', function() {
        var $httpBackend, getResponse;
        var item = {id: 1, name: 'Foo1'};

        beforeEach(inject(function(_$httpBackend_) {
            $httpBackend = _$httpBackend_;
            $httpBackend.expectGET('/foo/api/' + item.id).respond(item);
        }));

        beforeEach(function(done) {
            var crudService = CrudServiceFactory('/foo/api');

            crudService.get(item.id).then(function(response) {
               getResponse = response;
               done();
            });
            $httpBackend.flush();
        });

        it('should return the item correctly', function() {
            expect(getResponse.id).toEqual(item.id);
            expect(getResponse.name).toEqual(item.name);
        });
    });

    describe('create method ', function() {
        var $httpBackend, createResponse;
        var item = {id: 1, name: 'Foo1'};

        beforeEach(inject(function(_$httpBackend_) {
            $httpBackend = _$httpBackend_;
            $httpBackend.expectGET('/foo/api/create').respond(item);
        }));

        beforeEach(function(done) {
            var crudService = CrudServiceFactory('/foo/api');

            crudService.create().then(function(response) {
                createResponse = response;
                done();
            });

            $httpBackend.flush();
        });

        it('should return the item correctly', function() {
            expect(createResponse.id).toEqual(item.id);
			expect(createResponse.name).toEqual(item.name);
        });
    });

});
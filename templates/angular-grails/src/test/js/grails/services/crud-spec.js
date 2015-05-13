describe('grails CrudResourceFactory: ', function() {
    var CrudResourceFactory;

    beforeEach(module('grails.services.crud'));
    beforeEach(module(function($provide) {
        $provide.value('rootUrl', '/');
    }));
    beforeEach(inject(function(_CrudResourceFactory_) {
        CrudResourceFactory = _CrudResourceFactory_;
    }));


    it('should be able to create multiple CrudResource objects', function () {
        var crudResource1 = CrudResourceFactory('/api/foo1', 'Foo1');
        var crudResource2 = CrudResourceFactory('/api/foo1', 'Foo2');

        expect(crudResource1.getName()).toEqual('Foo1');
        expect(crudResource2.getName()).toEqual('Foo2');

        expect(crudResource1).not.toBe(crudResource2);
    });

    describe('list method ', function() {
        var itemList = [
            {id: 1, name: 'Foo1'},
            {id: 2, name: 'Foo2'}
        ];
        var $httpBackend, listResponse;

        beforeEach(inject(function(_$httpBackend_) {
            $httpBackend = _$httpBackend_;
            $httpBackend.expectGET('/foo/api').respond(itemList, {'Content-Range': '1-2/10'});
        }));

        beforeEach(function(done) {
            var crudResource = CrudResourceFactory('/foo/api', 'Foo');

            crudResource.list().then(function(response) {
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
            var crudResource = CrudResourceFactory('/foo/api', 'Foo');

            crudResource.get(item.id).then(function(response) {
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
            var crudResource = CrudResourceFactory('/foo/api', 'Foo');

            crudResource.create().then(function(response) {
                createResponse = response;
                done();
            });

            $httpBackend.flush();
        });

        it('should return the item correctly', function() {
            expect(createResponse).toEqual(item);
        });
    });

    describe('delete method ', function() {
        var $httpBackend, deleteResponse;
        var itemId = 1;

        beforeEach(inject(function(_$httpBackend_) {
            $httpBackend = _$httpBackend_;
            $httpBackend.expectDELETE('/foo/api/' + itemId).respond({success: true});
        }));

        beforeEach(function(done) {
            var crudResource = CrudResourceFactory('/foo/api', 'Foo');

            crudResource.delete(itemId).then(function(response) {
                deleteResponse = response;
                done();
            });
            $httpBackend.flush();
        });

        it('should delete the item correctly', function() {
            expect(deleteResponse.success).toEqual(true);
        });
    });

    describe('save method ', function() {
        var $httpBackend, saveResponse;
        var item = {id: 1, name: 'Foo'};

        beforeEach(inject(function(_$httpBackend_) {
            $httpBackend = _$httpBackend_;
            $httpBackend.expectPOST('/foo/api/' + item.id).respond(item);
        }));

        beforeEach(function(done) {
            var crudResource = CrudResourceFactory('/foo/api', 'Foo');

            crudResource.save(item).then(function(response) {
                saveResponse = response;
                done();
            });
            $httpBackend.flush();
        });

        it('should save the item correctly', function() {
            expect(saveResponse.id).toEqual(item.id);
            expect(saveResponse.name).toEqual(item.name);
        });
    });

    describe('save method ', function() {
        var $httpBackend, saveResponse;

        var item = {name: 'Foo'};
        var savedItem = {id: 1, name: 'Foo'};

        beforeEach(inject(function(_$httpBackend_) {
            $httpBackend = _$httpBackend_;
            $httpBackend.expectPOST('/foo/api').respond(savedItem);
        }));

        beforeEach(function(done) {
            var crudResource = CrudResourceFactory('/foo/api', 'Foo');

            crudResource.save(item).then(function(response) {
                saveResponse = response;
                done();
            });
            $httpBackend.flush();
        });

        it('should save the item correctly', function() {
            expect(saveResponse.id).toEqual(savedItem.id);
            expect(saveResponse.name).toEqual(savedItem.name);
        });
    });

    describe('update method ', function() {
        var $httpBackend, updateResponse;

        var item = {id: 1, name: 'Foo'};

        beforeEach(inject(function(_$httpBackend_) {
            $httpBackend = _$httpBackend_;
            $httpBackend.expectPUT('/foo/api/' + item.id).respond(item);
        }));

        beforeEach(function(done) {
            var crudResource = CrudResourceFactory('/foo/api', 'Foo');

            crudResource.update(item).then(function(response) {
                updateResponse = response;
                done();
            });
            $httpBackend.flush();
        });

        it('should save the item correctly', function() {
            expect(updateResponse.id).toEqual(item.id);
            expect(updateResponse.name).toEqual(item.name);
        });
    });

});
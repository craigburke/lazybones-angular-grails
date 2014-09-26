describe('grails crudButton: ', function() {
	
	var _message = null;
	var $rootScope, $compile, $location, $httpBackend, mockFlashService, DefaultResource;
	var item = {id: 1, name: 'Foo'};
	var afterFn = jasmine.createSpy('afterFn');

	beforeEach(module('grails.directives.buttons'));
	beforeEach(module('grails.services.crud'));
	beforeEach(module(function($provide) {
	     mockFlashService = {
	         clear: function() {
	             _message = null;
	         },
	         get: function() {
	             return _message;
	         },
	         set: function(message) {
	             _message = {message: message};
	         }
	     };

	     $provide.value('rootUrl', '/');
	     $provide.value('DefaultResource', DefaultResource);
	     $provide.value('defaultCrudResource', 'DefaultResource');
	     $provide.value('FlashService', mockFlashService);
	}));

	beforeEach(inject(function(_$rootScope_, _$compile_, _$httpBackend_, _$location_, CrudResourceFactory) {
	     $rootScope = _$rootScope_;
	     $compile = _$compile_;
	     $httpBackend = _$httpBackend_;
	     $location = _$location_;
	     DefaultResource = CrudResourceFactory('/api/foo', 'Foo');
	     afterFn.calls.reset();
	}));
   
	
    describe('create button: ', function() {
        var directiveScope;

        beforeEach(function () {
            $httpBackend.expectGET('create-button.html').respond("<button>Create</button>");

            var scope = $rootScope.$new();
            scope.afterAction = afterFn;
            var element = $compile("<a crud-button='create' after-action='afterAction()'></div>")(scope);
            scope.$digest();
            $httpBackend.flush();

            directiveScope = element.isolateScope();
		});

        it('should be redirected to /create on click', function() {
            directiveScope.onClick();
            expect(afterFn.calls.count()).toBe(1);
            expect($location.path()).toBe('/create');
        });
    });

    describe('edit button: ', function() {
        var directiveScope;

        beforeEach(function () {
            $httpBackend.expectGET('edit-button.html').respond("<button>Edit</button>");

            var scope = $rootScope.$new();
            scope.item = item;
            scope.afterAction = afterFn;
            var element = $compile("<a crud-button='edit' item='item' after-action='afterAction()'></div>")(scope);
            scope.$digest();
            $httpBackend.flush();

            directiveScope = element.isolateScope();
        });

        it('should be redirected to /edit on click', function() {
            directiveScope.onClick();
            expect(afterFn.calls.count()).toBe(1);
            expect($location.path()).toBe('/edit/1');
        });
    });
	


});
describe('grails flashDirective: ', function() {
    var $rootScope, mockFlashService, directiveScope;
    var message = {message: 'MESSAGE', title: 'TITLE', type: 'error'};

    beforeEach(module('grails.directives.flash'));
    beforeEach(module(function($provide) {
        mockFlashService = {
            clear: function() {},
            get: function() {
                return message;
            }
        };

        $provide.value('FlashService', mockFlashService);
    }));

    beforeEach(inject(function(_$rootScope_, $compile, $httpBackend) {
        $rootScope = _$rootScope_;
        $httpBackend.expectGET('flash-message.html').respond("<div>{{message}}</div>");

        var scope = $rootScope.$new();
        var element = $compile("<div flash-message></div>")(scope);
        scope.$digest();
        $httpBackend.flush();

        directiveScope = element.isolateScope();
    }));

    it('should be able to create a flash directive', function() {
        spyOn(mockFlashService, 'clear');
        directiveScope.close();
        expect(mockFlashService.clear).toHaveBeenCalled();
    });

    it('should be cleaned up when the scope is destroyed', function() {
        spyOn(mockFlashService, 'clear');
        expect(mockFlashService.clear).not.toHaveBeenCalled();

        directiveScope.$destroy();
        expect(mockFlashService.clear).toHaveBeenCalled();
    });

    it('should get message when flash:messageChange event is broadcast', function() {
        spyOn(mockFlashService, 'get').and.callThrough();
        expect(mockFlashService.get).not.toHaveBeenCalled();

        $rootScope.$broadcast('flash:messageChange');
        $rootScope.$digest();

        expect(mockFlashService.get).toHaveBeenCalled();
        expect(directiveScope.flash).toBe(message);
    })

});
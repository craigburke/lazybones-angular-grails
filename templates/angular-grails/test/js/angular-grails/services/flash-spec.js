describe('angularGrails FlashService: ', function() {
    var $rootScope, FlashService;

    beforeEach(module('angularGrails.services.flash'));
    beforeEach(inject(function(_FlashService_, _$rootScope_) {
        FlashService = _FlashService_;
        $rootScope = _$rootScope_;
    }));

    it('should be able to set a default message', function() {
        FlashService.set("Default");
        var message = FlashService.get();

        expect(message.message).toEqual("Default");
        expect(message.title).toBeUndefined();
        expect(message.type).toEqual(FlashService.TYPES.INFO);
    });

    it('should be able to set different message types', function() {
        FlashService.set("Error", FlashService.TYPES.ERROR, "Error Title");
        var message = FlashService.get();

        expect(message.message).toEqual("Error");
        expect(message.title).toEqual("Error Title");
        expect(message.type).toEqual(FlashService.TYPES.ERROR);

        FlashService.clear();
        expect(FlashService.get()).toBeNull();

        FlashService.set("Success", FlashService.TYPES.SUCCESS, "Success Title");
        var message = FlashService.get();

        expect(message.message).toEqual("Success");
        expect(message.title).toEqual("Success Title");
        expect(message.type).toEqual(FlashService.TYPES.SUCCESS);

        FlashService.clear();
        expect(FlashService.get()).toBeNull();

        FlashService.set("Info", FlashService.TYPES.INFO, "Info Title");
        var message = FlashService.get();

        expect(message.message).toEqual("Info");
        expect(message.title).toEqual("Info Title");
        expect(message.type).toEqual(FlashService.TYPES.INFO);

        FlashService.clear();
        expect(FlashService.get()).toBeNull();
    });

    it('should fire the flash:messageChange event', function() {
        spyOn($rootScope, '$broadcast');
        expect($rootScope.$broadcast).not.toHaveBeenCalledWith('flash:messageChange');

        FlashService.set("Foo");
        expect($rootScope.$broadcast).toHaveBeenCalledWith('flash:messageChange');
    });

});
describe('grails FlashService: ', function() {
    var $rootScope, FlashService;

    beforeEach(module('grails.services.flash'));
    beforeEach(inject(function(_FlashService_, _$rootScope_) {
        FlashService = _FlashService_;
        $rootScope = _$rootScope_;
    }));

    it('should be able to set a default message', function() {
        FlashService.setMessage("Default");
        var message = FlashService.getMessage();

        expect(message.message).toEqual("Default");
        expect(message.type).toEqual(FlashService.TYPE.INFO);
    });

    it('should be able to set different message types', function() {
        FlashService.error("Error");
        var message = FlashService.getMessage();

        expect(message.message).toEqual("Error");
        expect(message.type).toEqual(FlashService.TYPE.ERROR);

        FlashService.clear();
        expect(FlashService.getMessage()).toBeNull();

        FlashService.success("Success");
        var message = FlashService.getMessage();

        expect(message.message).toEqual("Success");
        expect(message.type).toEqual(FlashService.TYPE.SUCCESS);

        FlashService.clear();
        expect(FlashService.getMessage()).toBeNull();

        FlashService.setMessage("Info");
        var message = FlashService.getMessage();

        expect(message.message).toEqual("Info");
        expect(message.type).toEqual(FlashService.TYPE.INFO);

        FlashService.clear();
        expect(FlashService.getMessage()).toBeNull();
    });
	
	it('should clear after two clear request if routeChange property set', function() {
        FlashService.setMessage("Foo", {type: FlashService.TYPE.SUCCESS, routeChange: true});
		expect(FlashService.getMessage()).toBeNull();
		
		FlashService.clear();
		expect(FlashService.getMessage()).not.toBeNull();
		expect(FlashService.getMessage().message).toEqual("Foo");
		
		FlashService.clear();
		expect(FlashService.getMessage()).toBeNull();
		
        FlashService.setMessage("Foo");
        var message = FlashService.getMessage();
		expect(FlashService.getMessage().message).toEqual("Foo");
		
		FlashService.clear();
		expect(FlashService.getMessage()).toBeNull();	
	})

    it('should fire the flash:messageChange event', function() {
        spyOn($rootScope, '$broadcast');
        expect($rootScope.$broadcast).not.toHaveBeenCalledWith('flash:messageChange');

        FlashService.setMessage("Foo");
        expect($rootScope.$broadcast).toHaveBeenCalledWith('flash:messageChange');
    });

});
describe('grails fields: ', function() {

    var $rootScope, $compile, $httpBackend;

    beforeEach(module('ui.bootstrap'));
    beforeEach(module('grails.directives.fields'));

    beforeEach(inject(function(_$rootScope_, _$compile_, _$httpBackend_) {
        $rootScope = _$rootScope_;
        $compile = _$compile_;
        $httpBackend = _$httpBackend_;
    }));

    describe('date-field', function() {
        var directiveScope;

        beforeEach(function () {
            $httpBackend.expectGET('date-field.html').respond("<input datepicker-popup='' ng-model='ngModel' />");

            var scope = $rootScope.$new();
            var element = $compile("<input date-field></div>")(scope);
            scope.$digest();
            $httpBackend.flush();

            directiveScope = element.scope();
        });

        it('should set isOpened when open is called', function() {
            expect(directiveScope.opened).toBeFalsy();
            directiveScope.open();
            directiveScope.$digest();

            expect(directiveScope.opened).toBe(true);
        });


    });


});
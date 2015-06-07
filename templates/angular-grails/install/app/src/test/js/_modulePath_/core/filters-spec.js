describe('domainClassList filter: ', function() {
    var domainClassListFilter;

    beforeEach(module('${baseModule}.core.filters'));
    beforeEach(inject(function(_domainClassListFilter_) {
        domainClassListFilter = _domainClassListFilter_;
    }));

    it('should be able to convert arrays to a String of comma separated values', function () {
		expect(domainClassListFilter([])).toBe('');
		
		var domainObjectList = [{name:'Foo', toText:'foo'}, {name:'Foo2', toText:'foo2'}, {name:'Bar!', toText:'bar'}];
		expect(domainClassListFilter(domainObjectList)).toBe('foo, foo2, bar');
	
        expect(domainClassListFilter(null)).toBe('');
        expect(domainClassListFilter(undefined)).toBe('');
    });

});
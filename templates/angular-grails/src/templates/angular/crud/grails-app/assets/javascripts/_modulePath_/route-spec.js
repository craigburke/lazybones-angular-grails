describe('${domainClassName} Routes: ', function() {
    beforeEach(module('${fullModuleName}'));
	var \$location, \$state, \$rootScope, \$q;

	beforeEach(module(function(\$provide) {          
		var ServiceMock = {
			list: function() {
				var deferred = \$q.defer();
				deferred.resolve([]);
				return deferred.promise;
			},
			get: function() {
				var deferred = \$q.defer();
				deferred.resolve({});
				return deferred.promise;
			},
			create: function() {
				var deferred = \$q.defer();
				deferred.resolve({});
				return deferred.promise;
			}
		}
		\$provide.value('${domainClassName}Service', ServiceMock);				
<%= domainProperties.take(4).findAll{ it.isDomainClass }.collect { "\t\t\$provide.value('${it.classNameLowerCase.capitalize()}Service', ServiceMock);" }.join('\n') %>	
	}));
	
	beforeEach(inject(
		function(_\$location_, _\$state_, _\$rootScope_, _\$q_) {
			\$location = _\$location_;
			\$state = _\$state_;
			\$rootScope = _\$rootScope_;
			\$q = _\$q_;
		}	
	));
	
	describe('Index route: ', function() {
		beforeEach(inject(function(\$templateCache) {
			\$templateCache.put('/${modulePath}/list.html', 'list page');
		}));
		
		it('should load the list page by default', function() { 
			\$location.path('/${domainClassNameLowerCase}/'); 
			\$rootScope.\$digest(); 
			expect(\$state.current.name).toBe('${domainClassNameLowerCase}.list');
		});
		
		it('should load the list page on successful load of /list', function() {
			\$location.path('/${domainClassNameLowerCase}/list');
			\$rootScope.\$digest();
			expect(\$state.current.name).toBe('${domainClassNameLowerCase}.list');
		});
		
	});

	describe('Create route: ', function() {
		beforeEach(inject(function(\$templateCache) {
			\$templateCache.put('/${modulePath}/create-edit.html', 'create page');
		}));
		
		it('should load the create page on successful load of /create', function() {
			\$location.path('/${domainClassNameLowerCase}/create');
			\$rootScope.\$digest();
			expect(\$state.current.name).toBe('${domainClassNameLowerCase}.create');
		});
	});
	
	describe('Edit route: ', function() {
		beforeEach(inject(function(\$templateCache) {
			\$templateCache.put('/${modulePath}/create-edit.html', 'edit page');
		}));
		
		it('should load the edit page on successful load of /edit', function() {
			\$location.path('/${domainClassNameLowerCase}/edit/1');
			\$rootScope.\$digest();
			expect(\$state.current.name).toBe('${domainClassNameLowerCase}.edit');
		});
	});	
	
	describe('Show route: ', function() {
		beforeEach(inject(function(\$templateCache) {
			\$templateCache.put('/${modulePath}/show.html', 'show page');
		}));
		
		it('should load the show page on successful load of /show', function() {
			\$location.path('/${domainClassNameLowerCase}/show/1');
			\$rootScope.\$digest();
			expect(\$state.current.name).toBe('${domainClassNameLowerCase}.show');
		});
	});	
	
});
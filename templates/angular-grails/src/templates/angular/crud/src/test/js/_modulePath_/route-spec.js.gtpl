describe('${resourceName} Routes: ', function() {
    beforeEach(module('${fullModuleName}'));
	var location, route, rootScope;

	beforeEach(module(function(${DOLLAR_SIGN}provide) {          
		${DOLLAR_SIGN}provide.value('rootUrl', '/');
	}));	
	
	
	beforeEach(inject(
		function(${DOLLAR_SIGN}location, ${DOLLAR_SIGN}route, ${DOLLAR_SIGN}rootScope) {
			location = ${DOLLAR_SIGN}location;
			route = ${DOLLAR_SIGN}route;
			rootScope = ${DOLLAR_SIGN}rootScope;
		}	
	));
	
	describe('Index route: ', function() {
		beforeEach(inject(
			function(${DOLLAR_SIGN}httpBackend) {
				${DOLLAR_SIGN}httpBackend.whenGET('list.html').respond(200, 'list page');
				${DOLLAR_SIGN}httpBackend.whenGET('/api/${moduleName}').respond([]);
<%= domainProperties.take(4).findAll{ it.domainClass }.collect { "${TAB*4}${DOLLAR_SIGN}httpBackend.whenGET('/api/${it.name}').respond([]);" }.join(NEWLINE) %>
			}
		));
		
		it('should load the list page on successful load of /', function() {
			location.path('/');
			rootScope.${DOLLAR_SIGN}digest();
			expect(route.current.controller).toBe('ListCtrl as ctrl');
		});
		
		it('should redirect to the list page on non-existent route', function() { 
			location.path('bogus/route/foo/bar'); 
			rootScope.${DOLLAR_SIGN}digest(); 
			expect(route.current.controller).toBe('ListCtrl as ctrl');
		});
	
		it('should load the list page on successful load of /', function() {
			location.path('/');
			rootScope.${DOLLAR_SIGN}digest();
			expect(route.current.controller).toBe('ListCtrl as ctrl');
		});
	
	});

	describe('Create route: ', function() {
		beforeEach(inject(
			function(${DOLLAR_SIGN}httpBackend) {
				${DOLLAR_SIGN}httpBackend.whenGET('create-edit.html').respond(200, 'list page');
				${DOLLAR_SIGN}httpBackend.whenGET('/api/${moduleName}/create').respond([]);
<%= domainProperties.take(4).findAll{ it.domainClass }.collect { "${TAB*4}${DOLLAR_SIGN}httpBackend.whenGET('/api/${it.name}').respond([]);" }.join(NEWLINE) %>
			}
		));
		
		it('should load the create page on successful load of /create', function() {
			location.path('/create');
			rootScope.${DOLLAR_SIGN}digest();
			expect(route.current.controller).toBe('CreateEditCtrl as ctrl');
		});
	
	});
	
	describe('Edit route: ', function() {
		beforeEach(inject(
			function(${DOLLAR_SIGN}httpBackend) {
				${DOLLAR_SIGN}httpBackend.whenGET('create-edit.html').respond(200, 'list page');
				${DOLLAR_SIGN}httpBackend.whenGET('/api/${moduleName}/1').respond([]);
<%= domainProperties.take(4).findAll{ it.domainClass }.collect { "${TAB*4}${DOLLAR_SIGN}httpBackend.whenGET('/api/${it.name}').respond([]);" }.join(NEWLINE) %>
			}
		));
		
		it('should load the edit page on successful load of /edit', function() {
			location.path('/edit/1');
			rootScope.${DOLLAR_SIGN}digest();
			expect(route.current.controller).toBe('CreateEditCtrl as ctrl');
		});
	
	});	
	

});
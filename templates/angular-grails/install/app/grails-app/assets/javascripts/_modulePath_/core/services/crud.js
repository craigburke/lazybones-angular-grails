'use strict';

function CrudServiceFactory(rootUrl, Restangular, \$q, \$http) {

	var CustomRestangular = Restangular.withConfig(function(RestangularConfigurer) { });
	
	CustomRestangular.addResponseInterceptor(function(data, operation, what, url, response, deferred) {
		if (operation === "getList") {
		    var totalCount = response.headers('x-item-range').split('/')[1];
		    totalCount = parseInt(totalCount);
			
		    data.getTotalCount = function() {
		    	return totalCount;
		    };
		}
		return data;
	});
	
    return function(restUrl) {
        var crudResource = {};
        var baseUrl = (rootUrl + restUrl).replace('//', '/');
		var resource = CustomRestangular.all(baseUrl);

		var chainPromise = function(promise, successFn, errorFn) {
			if (successFn && errorFn) {
				promise = promise.then(successFn, errorFn);
			}
			else if (successFn) {
				promise = promise.then(successFn);
			}

			return promise;
		};

        crudResource.list = function(params, successFn, errorFn) {
			params = params || {};
			var queryParams = angular.copy(params);
			delete queryParams.filter;
			
			angular.forEach(params.filter, function(value, key) {
				queryParams['filter.' + key] = value;
			});
			
            return chainPromise(resource.getList(queryParams), successFn, errorFn);
        };

        crudResource.get = function(id, successFn, errorFn) {
        	return chainPromise(resource.get(id), successFn, errorFn);
		};

        crudResource.create = function(successFn, errorFn) {
            var deferred = \$q.defer();

            \$http.get(baseUrl + "/create").success(function(data) {
				var item = Restangular.restangularizeElement(null, data, baseUrl);
                deferred.resolve(item);
            });

            return chainPromise(deferred.promise, successFn, errorFn);
        };

        return crudResource;
    };
}

angular.module('${baseModule}.core.services.crud', ['restangular'])
    .factory('CrudServiceFactory', CrudServiceFactory);
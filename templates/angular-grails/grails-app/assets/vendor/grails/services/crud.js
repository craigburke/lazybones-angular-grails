'use strict';

function CrudServiceFactory(rootUrl, Restangular, $q, $http) {

    return function(restUrl) {
        var crudResource = {};
        var baseUrl = (rootUrl + restUrl).replace('//', '/');
		var resource = Restangular.all(baseUrl);

        crudResource.list = function(params, successFn, errorFn) {
            return resource.getList();
        };

        crudResource.get = function(id, successFn, errorFn) {
        	return resource.get(id).then(successFn, errorFn);
		};

        crudResource.create = function(successFn, errorFn) {
            var deferred = $q.defer();

            $http.get(baseUrl + "/create").success(function(data) {
                deferred.resolve(data);
            });

            return chainPromise(deferred.promise, successFn, errorFn);
        };

        crudResource.delete = function(id, successFn, errorFn) {
            //return getResourcePromise(resource.delete({id: id}), successFn, errorFn);
        };

        crudResource.save = function(data, successFn, errorFn) {
            //return getResourcePromise(resource.save(data), successFn, errorFn);
        };

        crudResource.update = function(data, successFn, errorFn) {
            //return getResourcePromise(resource.update(data), successFn, errorFn);
        };


        return crudResource;
    };
}

angular.module('grails.services.crud', ['restangular'])
    .factory('CrudServiceFactory', CrudServiceFactory);
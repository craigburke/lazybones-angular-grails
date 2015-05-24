'use strict';

function ${angularResource}(CrudResourceFactory) {
    return CrudResourceFactory('${angularResourceUrl}', '${resourceName}');
}

angular.module('${fullModuleName}.services', ['grails'])
    .factory('${angularResource}', ${angularResource});

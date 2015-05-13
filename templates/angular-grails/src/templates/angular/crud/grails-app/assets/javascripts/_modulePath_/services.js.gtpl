'use strict';

function ${defaultResource}(CrudResourceFactory) {
    return CrudResourceFactory('${resourceUrl}', '${resourceName}');
}

angular.module('${fullModuleName}.services', ['grails'])
    .factory('${defaultResource}', ${defaultResource});

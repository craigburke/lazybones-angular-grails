'use strict';

function ${defaultResource}(CrudResourceFactory) {
    return CrudResourceFactory('${resourceUrl}', '${resourceName}');
}

angular.module('${moduleName}.services', ['grails'])
    .factory('${defaultResource}', ${defaultResource});

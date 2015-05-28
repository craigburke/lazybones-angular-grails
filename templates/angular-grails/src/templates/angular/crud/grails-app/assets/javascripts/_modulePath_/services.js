'use strict';

function ${angularResource}(CrudResourceFactory) {
    return CrudResourceFactory('${angularResourceUrl}', '${domainClassName}');
}

angular.module('${fullModuleName}.services', ['grails'])
    .factory('${angularResource}', ${angularResource});

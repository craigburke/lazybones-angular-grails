'use strict';

function ${defaultResource}(CrudResourceFactory) {
    return CrudResourceFactory('${resourceUrl}', '${resourceName}');
}

angular.module('${moduleName}.services', ['angularGrails'])
    .factory('${defaultResource}', ${defaultResource});

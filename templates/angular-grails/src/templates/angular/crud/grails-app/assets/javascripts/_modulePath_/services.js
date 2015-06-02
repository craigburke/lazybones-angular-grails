'use strict';

function ${domainClassName}Service(CrudServiceFactory) {
    return CrudServiceFactory('${angularResourceUrl}');
}

angular.module('${fullModuleName}.services', [])
    .factory('${domainClassName}Service', ${domainClassName}Service);

'use strict';

function ${domainClassName}Service(CrudServiceFactory) {
    return CrudServiceFactory('${angularResourceUrl}');
}

angular.module('${fullModuleName}.services', ['grails'])
    .factory('${domainClassName}Service', ${domainClassName}Service);

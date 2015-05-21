//= require_self
//= require controllers
//= require_tree /${modulePath}/templates/

'use strict';
angular.module('${fullModuleName}', ['grails', '${fullModuleName}.controllers'])
.config(function(\$routeProvider) {
\$routeProvider
        .when('/', {
            controller: 'MainCtrl as ctrl',
            templateUrl: 'index.html',
        })
        .otherwise({redirectTo: '/'});
});

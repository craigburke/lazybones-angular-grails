//= require angular/angular
//= require_full_tree /angular/modules
//= require restangular/restangular
//= require lodash/lodash
//= require ui-router/angular-ui-router
//= require angular-bootstrap/ui-bootstrap-tpls
//= require directives/module
//= require services/module
//= require filters

'use strict';

angular.module('${baseModule}.core', [
    'ngAnimate',
    'restangular',
    'ui.router',
    'ui.bootstrap',
    '${baseModule}.core.directives',
    '${baseModule}.core.services',
    '${baseModule}.core.filters',
    '${baseModule}.core.constants'
]);

angular.module('${baseModule}.core.constants', []);
//= require /angular/angular
//= require_full_tree /angular/modules
//= require /restangular.min
//= require /underscore.min
//= require /angular-ui-router.min
//= require /ui-bootstrap.min
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
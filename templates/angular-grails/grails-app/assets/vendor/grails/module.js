//= require /angular/angular
//= require_tree /angular/modules
//= require restangular.min
//= require underscore.min
//= require angular-ui-router.min
//= require ui-bootstrap.min
//= require directives/module
//= require services/module

'use strict';

angular.module('grails', [
	'ngAnimate',
    'restangular',
    'ui.router',
    'ui.bootstrap',
    'grails.directives',
    'grails.services',
    'grails.constants'
]);

angular.module('grails.constants', []);
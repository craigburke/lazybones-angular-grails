//= require /angular/angular
//= require_tree /angular/modules
//= require directives/module
//= require services/module
//= require ui-bootstrap.min

'use strict';

angular.module('grails', [
    'ngRoute',
    'ngResource',
	'ngAnimate',
    'ui.bootstrap',
    'grails.directives',
    'grails.services',
    'grails.constants'
]);

angular.module('grails.constants', []);
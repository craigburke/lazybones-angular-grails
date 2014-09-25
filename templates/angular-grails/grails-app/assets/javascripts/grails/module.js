//= require /angular/angular
//= require_tree /angular/modules
//= require directives/module
//= require services/module
//= require ui-bootstrap.min
//= require controllers

'use strict';

angular.module('grails', [
    'ngRoute',
    'ngResource',
    'ui.bootstrap',
    'grails.directives',
    'grails.services',
    'grails.controllers',
    'grails.constants'
]);
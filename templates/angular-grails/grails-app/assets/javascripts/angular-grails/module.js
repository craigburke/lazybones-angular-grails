//= require /angular/angular
//= require_tree /angular/modules
//= require directives/module
//= require services/module
//= require ui-bootstrap.min
//= require controllers

'use strict';

angular.module('angularGrails', [
    'ngRoute',
    'ngResource',
    'ui.bootstrap',
    'angularGrails.directives',
    'angularGrails.services',
    'angularGrails.controllers',
    'angularGrails.constants'
]);
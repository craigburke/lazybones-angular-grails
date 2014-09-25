//= require /angular/angular
//= require_tree /angular/modules
//= require directives/module
//= require services/module
//= require ui-bootstrap.min
//= require controllers

'use strict';

angular.module('grailsCrud', [
    'ngRoute',
    'ngResource',
    'ui.bootstrap',
    'grailsCrud.directives',
    'grailsCrud.services',
    'grailsCrud.controllers',
    'grailsCrud.constants'
]);
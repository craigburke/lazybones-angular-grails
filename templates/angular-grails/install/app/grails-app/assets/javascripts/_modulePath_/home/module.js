//= require_self 
//= require routes
//= require services
//= require controllers
//= require_tree /${modulePath}/home/templates/

'use strict';

angular.module('${baseModule}.home', [
	'${baseModule}.home.services',
	'${baseModule}.home.controllers'
]);
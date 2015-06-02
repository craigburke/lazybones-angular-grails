'use strict';

function HomeCtrl(info) {
    var self = this;
    self.info = info;
}

angular.module('${baseModule}.home.controllers', [])
	.controller('HomeCtrl', HomeCtrl);
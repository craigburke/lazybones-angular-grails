'use strict';

angular.module('${baseModule}.home')
.config(function(\$stateProvider) {
	\$stateProvider
		.state('home', {
			url:'',
			templateUrl:'/${modulePath}/home/index.html',
			controller: 'HomeCtrl as ctrl',
			resolve: {
				info: function(HomeService) {
					return HomeService.getInfo();
				}
			}
		});
});
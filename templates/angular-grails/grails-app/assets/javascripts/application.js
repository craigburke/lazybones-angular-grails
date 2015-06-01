//= require grails/module
//= require ${modulePath}/home/module

'use strict';

angular.module('${baseModule}', [
	'${baseModule}.home'
])
.run(function(\$rootScope, \$state) {
	\$rootScope.\$state = \$state;
});
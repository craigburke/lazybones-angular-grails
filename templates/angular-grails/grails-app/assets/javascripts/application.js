//= require ${modulePath}/core/module
//= require ${modulePath}/home/module

'use strict';

angular.module('${baseModule}', [
	'${baseModule}.core',
	'${baseModule}.home'
])
.run(function(\$rootScope, \$state) {
	\$rootScope.\$state = \$state;
});
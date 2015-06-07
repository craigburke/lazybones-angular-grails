'use strict';

function domainClassList() {
	return function(list) {
		if (!angular.isArray(list) || list.length === 0) {
			return '';
		}
		else {
			return list.map(function(item) {
					return item.toText;
				}).join(', ');
		}
	}
}

angular.module('${baseModule}.core.filters', [])
    .filter('domainClassList', domainClassList);
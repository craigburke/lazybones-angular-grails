'use strict';

function domainClassList() {
	return function(list) {
		if (list.length === 0) {
			return list;
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
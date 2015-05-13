'use strict';
<% def domainList = domainProperties.findAll{ it.domainClass }.collect { "${it.name}List" } %>
function ListCtrl(${DOLLAR_SIGN}scope, ${defaultResource}, ${moduleName}List<%= (domainList ? ', ' : '') + domainList.join(', ') %>, pageSize) {
    var self = this;
    self.${moduleName}List = ${moduleName}List;
	<%= domainList.collect{ "self.${it} = ${it};" }.join('\n') %>
    self.pageSize = pageSize;
    self.page = 1;
    self.filter = {};

    <%='\\$scope.\\$watchCollection'%>(function() { return self.filter }, function() {
        self.reload();
    });

    self.load = function() {
        var params = {page: self.page};

        if (self.sort) {
            angular.extend(params, self.sort);
        }
		if (self.filter) {
			params.filter = self.filter
		}

        ${defaultResource}.list(params).then(function(items) {
            self.${moduleName}List = items;
        });
    };

    self.reload = function() {
        self.page = 1;
        self.load();
    }
}

function ShowCtrl(${moduleName}) {
    var self = this;
    self.${moduleName} = ${moduleName};
};

function CreateEditCtrl(${moduleName}<%= (domainList ? ', ' : '') + domainList.join(', ') %> ) {
    var self = this;
	<%= domainList.collect{ "self.${it} = ${it};" }.join('\n') %>
    self.${moduleName} = ${moduleName};
}

angular.module('${fullModuleName}.controllers', [])
    .controller('ListCtrl', ListCtrl)
    .controller('ShowCtrl', ShowCtrl)
    .controller('CreateEditCtrl', CreateEditCtrl);
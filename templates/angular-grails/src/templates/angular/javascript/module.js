//= require_self
//= require services
//= require_tree /templates/${modulePath}

'use strict';
angular.module('${moduleName}', ['angularGrails', '${moduleName}.services'])
.value('defaultCrudResource', '${defaultResource}')
.config(function(<%='$routeProvider'%>) {
<%='$routeProvider'%>
        .when('/', {
            controller: 'DefaultListCtrl as ctrl',
            templateUrl: 'list.html',
            resolve: {
                items: function(\$route, ${defaultResource}) {
                    var params = \$route.current.params;
                    return ${defaultResource}.list(params);
                }
            }
        })
        .when('/create', {
            controller: 'DefaultCreateEditCtrl as ctrl',
            templateUrl: 'create-edit.html',
            resolve: {
                item: function(${defaultResource}) {
                    return ${defaultResource}.create();
                }
            }
        })
        .when('/edit/:id', {
            controller: 'DefaultCreateEditCtrl as ctrl',
            templateUrl: 'create-edit.html',
            resolve: {
                item: function(\$route, ${defaultResource}) {
                    var id = \$route.current.params.id;
                    return ${defaultResource}.get(id);
                }
            }
        })
        .when('/show/:id', {
            controller: 'DefaultShowCtrl as ctrl',
            templateUrl: 'show.html',
            resolve: {
                item: function(\$route, ${defaultResource}) {
                    var id = \$route.current.params.id;
                    return ${defaultResource}.get(id);
                }
            }
        })
        .otherwise({redirectTo: '/'});
});

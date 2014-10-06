<div crud-breadcrumbs="list"></div>
<h2>${resourceName} List</h2>
<div flash-message ></div>

<p><button crud-button="create" ></button></p>

<table class="table table-striped table-bordered">

    <thead sort-header ng-model="ctrl.sort" on-sort="ctrl.reload()">
        <th sortable-column title="Id" property="id"></th>
		<% domainProperties.take(4).each { property -> %>
		<th sortable-column title="${property.label}" property="${property.name}"></th>
		<% } %>
        <th>&nbsp;</th>
    </thead>
    <tbody>
        <tr ng-repeat="item in ctrl.items">
            <td><a href="#/show/{{item.id}}">{{item.id}}</a></td>
			<% domainProperties.take(4).each { property -> %>
			<td>{{ item.${property.name}${property.displayFilter} }}</td>
			<% } %>
            <td>
                <button crud-button="edit" item="item" ></button>
                <button crud-button="delete" item="item" after-action="ctrl.reload()"></button>
            </td>
        </tr>
    </tbody>
</table>

<div pagination total-items="ctrl.items.getTotalCount()" ng-model="ctrl.page" ng-change="ctrl.load()"></div>

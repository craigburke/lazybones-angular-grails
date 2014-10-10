<div crud-breadcrumbs="{{ctrl.item.id ? 'edit' : 'create'}}"></div>
<h2>
    <span ng-hide="ctrl.item.id">Create ${resourceName}</span>
    <span ng-show="ctrl.item.id">Edit ${resourceName}</span>
</h2>

<div flash-message ></div>

<form name="form" class="form-horizontal" role="form" novalidate>
<% domainProperties.each { property -> %>
	<div field-container label="${property.label}" invalid="form.${property.name}.<%= '\\$invalid' %>">
		<%= generateInput(property, 'ctrl.item') %>
	</div>
<% } %>

    <div>
        <button crud-button="save" item="ctrl.item" is-disabled="form.<%= '\\$invalid' %>"></button>
        <span ng-if="ctrl.item.id"><button crud-button="delete" item="ctrl.item"></button></span>
    </div>

</form>
<div crud-breadcrumbs="{{ctrl.${moduleName}.id ? 'edit' : 'create'}}"></div>
<h2>
    <span ng-hide="ctrl.${moduleName}.id">Create ${resourceName}</span>
    <span ng-show="ctrl.${moduleName}.id">Edit ${resourceName}</span>
</h2>

<div flash-message ></div>

<form name="form" class="form-horizontal" role="form" novalidate> <% domainProperties.each { property -> %>
	
	<div field-container label="${property.label}" invalid="form.${property.name}.${DOLLAR_SIGN}invalid">
		<%= renderInput(property, 'ctrl.${moduleName}') %>
	</div><% } %>

    <div>
        <button crud-button="save" item="ctrl.${moduleName}" is-disabled="form.${DOLLAR_SIGN}invalid"></button>
        <span ng-if="ctrl.${moduleName}.id"><button crud-button="delete" item="ctrl.${moduleName}"></button></span>
    </div>

</form>
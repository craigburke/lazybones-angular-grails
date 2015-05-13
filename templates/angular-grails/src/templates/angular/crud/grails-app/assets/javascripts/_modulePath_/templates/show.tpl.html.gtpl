<div crud-breadcrumbs="show"></div>
<h2>Show ${resourceName}</h2>
<div flash-message ></div>

<table class="table table-bordered table-striped"><% domainProperties.each { property -> %>
    <tr display-field label="${property.label}" value="${renderDisplay(property, 'ctrl.' + moduleName)}"></tr><% } %>
</table>

<div class="form-actions">
    <button crud-button="edit" item="ctrl.${moduleName}"></button>
    <button crud-button="delete" item="ctrl.${moduleName}" ></button>
</div>
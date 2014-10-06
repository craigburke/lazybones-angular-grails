<div crud-breadcrumbs="show"></div>
<h2>Show ${resourceName}</h2>
<div flash-message ></div>

<table class="table table-bordered">
<% domainProperties.each { property -> %>
    <tr display-field label="${property.label}" value="ctrl.item.${property.name}${property.displayFilter}"></tr>
<% } %>

</table>

<div class="form-actions">
    <button crud-button="edit" item="ctrl.item"></button>
    <button crud-button="delete" item="ctrl.item" ></button>
</div>
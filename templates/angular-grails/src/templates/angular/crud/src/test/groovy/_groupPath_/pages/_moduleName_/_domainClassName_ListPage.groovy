package ${group}.pages.${moduleName}

import geb.Module
import geb.Page

class ${domainClassName}ListPage extends Page {

    static url = "#/${moduleName}/list"

    static at = { \$('h2').text() == '${domainClassName} List' }

    static content = {
<%= domainProperties.collect {"\t\t${it.name}Filter {\$(\"${it.domainClass ? 'select' : 'input'}[ng-model='ctrl.filter.${it.name + (it.domainClass ? 'Id' : '')}']\")}" }.join('\n') %>
	
<%= domainProperties.take(4).collect{ """\t\t${it.name}Sort { \$("table#list th[property='${it.name}']") }"""}.join('\n') %>
    
	    createButton { \$("button[crud-button='create']") }
        successMessage { \$(".alert-success") }
		
        rows { moduleList ${domainClassName}ListRow, \$("table#list tbody tr") }
    }

}

class ${domainClassName}ListRow extends Module {

	static content = {
		cell { \$("td") }
        editButton { \$("button[crud-button='edit']") }
        deleteButton { \$("button[crud-button='delete']") }
    }

}
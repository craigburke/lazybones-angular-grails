package ${group}.pages.${moduleName}

import geb.Module
import geb.Page

class ${resourceName}EditPage extends Page {

    static url = "${moduleName}#/create"

    static at = { \$('h2').text() == 'Edit ${resourceName}' }

    static content = {
<%= domainProperties.collect {"\t\t${it.name}Field {\$(\"${it.domainClass ? 'select' : 'input'}[ng-model='ctrl.${moduleName}.${it.name}']\")}" }.join('\n') %>
        saveButton { \$('button[crud-button="save"]') }
    }

}
package ${group}.pages.${moduleName}

import geb.Module
import geb.Page

class ${domainClassName}EditPage extends Page {

    static at = { \$('h2').text() == 'Edit ${domainClassName}' }

    static content = {
<%= domainProperties.collect {"\t\t${it.name}Field {\$(\"${it.domainClass ? 'select' : 'input'}[ng-model='ctrl.${moduleName}.${it.name}']\")}" }.join('\n') %>
        saveButton { \$('button[crud-button="save"]') }
    }

}
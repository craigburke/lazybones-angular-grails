package ${group}.pages.${moduleName}

import geb.Module
import geb.Page

class ${resourceName}EditPage extends Page {

    static url = "${moduleName}#/create"

    static at = { ${DOLLAR_SIGN}('h2').text() == 'Edit ${resourceName}' }

    static content = {
<%= domainProperties.collect {"${TAB*2}${it.name}Field {${DOLLAR_SIGN}(\"${it.domainClass ? 'select' : 'input'}[ng-model='ctrl.${moduleName}.${it.name}']\")}" }.join(NEWLINE) %>
        saveButton { ${DOLLAR_SIGN}('button[crud-button="save"]') }
    }

}
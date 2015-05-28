package ${group}.pages.${moduleName}

import geb.Page

class ${domainClassName}ShowPage extends Page {

    static at = { \$('h2').text().startsWith 'Show ${domainClassName}' }

    static content = {
        successMessage { \$(".alert-success") }
    }

}
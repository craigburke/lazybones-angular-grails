package ${group}.pages.${moduleName}

import geb.Page

class ${resourceName}ShowPage extends Page {

    static at = { \$('h2').text().startsWith 'Show ${resourceName}' }

    static content = {
        successMessage { \$(".alert-success") }
    }

}
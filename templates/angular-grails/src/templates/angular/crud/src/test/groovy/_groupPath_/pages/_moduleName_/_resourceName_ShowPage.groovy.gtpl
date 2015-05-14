package ${group}.pages.${moduleName}

import geb.Page

class ${resourceName}ShowPage extends Page {

    static at = { ${DOLLAR_SIGN}('h2').text().startsWith 'Show ${resourceName}' }

    static content = {
        successMessage { ${DOLLAR_SIGN}(".alert-success") }
    }

}
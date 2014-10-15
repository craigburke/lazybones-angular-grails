package ${group}.pages.${moduleName}

import geb.Module
import geb.Page

class ${resourceName}ListPage extends Page {

    static url = "${moduleName}"

    static at = { ${DOLLAR_SIGN}('h2').text() == '${resourceName} List' }

    static content = {
        createButton { ${DOLLAR_SIGN}("button[crud-button='create']") }
        successMessage { ${DOLLAR_SIGN}(".alert-success") }
        rows { moduleList ${resourceName}ListRow, ${DOLLAR_SIGN}("table tbody tr") }
    }

}

class ${resourceName}ListRow extends Module {

	static content = {
		cell { ${DOLLAR_SIGN}("td") }
        editButton {${DOLLAR_SIGN}("button[crud-button='edit']")}
        deleteButton {${DOLLAR_SIGN}("button[crud-button='delete']")}
    }

}
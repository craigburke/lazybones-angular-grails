def util = [:]

util.renderInput = { def property, String modelPrefix ->
	String attrsString = ""
	if (property.constraints.required && !property.constraints.nullable) {
		attrsString += " required "
	}
	if (property.isHasMany) {
		attrsString += " multiple "
	}
	
	if (property.isDomainClass) {
		String sourceList = property.classNameLowerCase + 'List'
	
		"""<select name="${property.name}" class="form-control" ng-model="${modelPrefix}.${property.name}"${attrsString} ng-options="item.toText for item in ctrl.${sourceList} track by item.id"  ></select>"""
	}
	else {
		String inputType = property.type in [Float, Integer] ? 'number' : 'text'
		"""<input name="${property.name}" type="${inputType}" class='form-control'${property.type == Date ? ' date-field ' : ' '}ng-model="${modelPrefix}.${property.name}"${attrsString} />"""
	}
}

util.renderFilter = { def property ->
	String attrsString = " ng-model-options=\"{ debounce: 300 }\" "
	
	if (property.isDomainClass) {
		String sourceList = property.classNameLowerCase + 'List'		
		String name = property.isHasMany ? property.name : "${property.name}Id"
		
		"""<select class="form-control" ng-model="ctrl.filter.${name}" ng-options="item.id as item.toText for item in ctrl.${sourceList}"${attrsString} ><option value="">-- Select ${property.label}--</option></select>"""
	}
	else {
		String inputType = property.type in [Float, Integer] ? 'number' : 'text'
		"""<input type="${inputType}" class='form-control'${property.type == Date ? ' date-field ' : ' '}ng-model="ctrl.filter.${property.name}"${attrsString} />"""
	}
}

util.renderDisplay = { def property, String modelPrefix ->
	String displayFilter = ""
	switch(property.type) {
		case Integer:
			displayFilter = " | number"
			break
		case Float:
			displayFilter = " | currency"
			break
		case Date:
			displayFilter = " | date: 'MMM d, yyyy'"
			break
	}	
	String item = "${modelPrefix}.${property.name}"
	
	if (property.isHasMany) {
		displayFilter = " | domainClassList"
	}
	else if (property.isDomainClass) {
		item += ".toText"
	}
	
	"${item}${displayFilter}"
}

return util
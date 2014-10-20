def util = [:]


util.renderInput = { property, String modelPrefix, attrs = [:] ->
	String attrsString = attrs.collect { key, value -> " ${key}=\"${value}\" " }.join("")
	if (property.constraints.required || !property.constraints.nullable) {
		attrsString += " required "
	}
	
	if (property.domainClass) {
		"""<select name="${property.name}" class="form-control" ng-model="${modelPrefix}.${property.name}"${attrsString} ng-options="item.toText for item in ctrl.${property.name}List track by item.id" ></select>"""
	}
	else {
		String inputType = property.type in [Float, Integer] ? 'number' : 'text'
		"""<input name="${property.name}" type="${inputType}" class='form-control'${property.type == Date ? ' date-field ' : ' '}ng-model="${modelPrefix}.${property.name}"${attrsString} />"""
	}
}

util.renderDisplay = { property, String modelPrefix ->
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
	if (property.domainClass) {
		item += ".toText"
	}
	
	"${item}${displayFilter}"
}

return util
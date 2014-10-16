import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import static groovy.io.FileType.FILES

def props = [:]

props.DOLLAR_SIGN = '\\$'
props.TAB = '\\t'
props.NEWLINE = '\\n'

props.renderInput = { property, String modelPrefix ->
	if (property.domainClass) {
		"""<select name="${property.name}" class="form-control" ng-model="${modelPrefix}.${property.name}" ng-options="item.toText for item in ctrl.${property.name}List track by item.id" ></select>"""
	}
	else {
		String inputType = property.type in [Float, Integer] ? 'number' : 'text'
		"""<input name="${property.name}" type="${inputType}" class='form-control'${property.type == Date ? ' date-field ' : ' '}ng-model="${modelPrefix}.${property.name}" />"""
	}
}

props.renderDisplay = { property, String modelPrefix ->
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

props.getModulePath = { String fullModule ->
	String path = fullModule.replace('.', '/')
	path = path.replaceAll(/([A-Z])/,/-$1/).toLowerCase().replaceAll(/^-/,'')
	path.replaceAll(/\/-/, '/')
}

props.formatModuleName = { String moduleName ->
	def moduleParts = moduleName.tokenize('.')	
	moduleParts.collect { it[0]?.toLowerCase() + it?.substring(1) }.join('.')
} 
 
boolean isCrudModule = (tmplQualifiers[0] != "blank")

props.group = parentParams.group
props.groupPath = props.group.replace('.', '/')
props.moduleName = props.formatModuleName(ask("Define the name for your new module [myModule]: ", "myModule", "moduleName"))
props.rootModule = parentParams.angularModule

props.resourceName = {
		String resource = it.tokenize('.').last()
		resource[0]?.toUpperCase() + resource?.substring(1)
}(props.moduleName)
	
props.fullModuleName = "${parentParams.angularModule}.${props.moduleName}"
props.modulePath = props.getModulePath(props.fullModuleName)

if (isCrudModule) {
	props.domainClassName = props.group + '.' + ask("Define the name of the domain class [Foo]: ", "Foo", "domainClass")
	props.domainProperties = getDomainProperties(props.domainClassName, props.group)
	props.defaultResource = "${props.resourceName}Resource"
	props.resourceUrl = "/api/${props.moduleName}"
}

String moduleFilesDir = "angular/" + (isCrudModule ? "crud" : "blank")

def copyAngularTemplates = {
	File source = new File(projectDir, "src/templates/angular/")	
	File destinatation = new File(templateDir, "angular/")	
	FileUtils.deleteQuietly(destinatation)
	FileUtils.copyDirectory(source, destinatation, true)
}

def generateCustomMarshaller = {	
	def customMarshallerRegistrar = new File(projectDir, "src/groovy/${props.groupPath}/CustomMarshallerRegistrar.groovy")
	
	String jsonMarshaller = 
	"""
		JSON.registerObjectMarshaller(${props.domainClassName}) {
			def map = [:];
			map['id'] = it?.id;
			${props.domainProperties.collect { "map['" + it.name + "'] = it?." + it.name + ";" }.join('\n\t\t\t')}
	    	map['toText'] = it?.toString();
			return map 
		}"""
		
	// Remove existing marshallers
	def functionRegex = /(?ms)(JSON\.registerObjectMarshaller\((.*?)\).*?return.*?\})/
	customMarshallerRegistrar.text = customMarshallerRegistrar.text.replaceAll(functionRegex) { all, function, matchedDomainClass -> 
		(matchedDomainClass == props.domainClassName) ? '' : all
	}
				
	// Add new marshaller
	customMarshallerRegistrar.text = customMarshallerRegistrar.text.replaceAll(/(?s)(registerMarshallers\(\).*?\{)/, "\$1\n${jsonMarshaller}")
}

def setUrlMappings = {
	def mappingFile = new File("${projectDir}/grails-app/conf/UrlMappings.groovy")
	
	String mapping = "\t\t'/${props.moduleName}'(view: '${props.moduleName}')\n"
	if (isCrudModule) {
		// Add resource mapping
		mapping += "\t\t'/api/${props.moduleName}'(resources: '${props.moduleName}')\n"
	}
		
	if (!mappingFile.text.contains(mapping)) {
		mappingFile.text = mappingFile.text.replaceAll(/(mappings\s*=\s*\{\s*\n*)/, "\$1${mapping}")
	}
}

def processTemplateFiles = {
	processTemplates "${moduleFilesDir}/**/*", props
	processTemplates "angular/common/**/*", props

	def processFile = { File baseDirectory, File file ->
		String relativePath = file.path - baseDirectory.path
	
		String destinationPath = relativePath

		def pathReplacements = [
			modulePath: props.modulePath,
			groupPath: props.groupPath,
			resourceName: props.resourceName,
			moduleName: props.moduleName
		]
				
		pathReplacements.each { key, value ->
			destinationPath = destinationPath.replace("_${key}_", value)
		}
		
		File destination = new File(projectDir, destinationPath)
		FileUtils.deleteQuietly(destination)
		FileUtils.copyFile(file, destination)
	}
	
	File templateDirectory = new File(templateDir, "${moduleFilesDir}")	
	templateDirectory.eachFileRecurse(FILES) { processFile(templateDirectory, it) }
	 
	File commonDirectory = new File(templateDir, "angular/common") 
	commonDirectory.eachFileRecurse(FILES) { processFile(commonDirectory, it) } 
}

def printMessage = {
	println "Your Angular app (${props.fullModuleName}) has been created"
	println "URL: /${props.moduleName}"
}

def cleanup = {
	FileUtils.deleteQuietly(new File(templateDir, "angular"))
}

copyAngularTemplates()

if (isCrudModule) {
	generateCustomMarshaller()
}

setUrlMappings()
processTemplateFiles()
printMessage()
cleanup()

def getDomainProperties(String className, String group) {
	def classLoader = new GroovyClassLoader()
	
	['grails-app/domain', 'grails-app/services', 'src/groovy', 'src/java'].each {
		classLoader.addClasspath(it)
	}
		
	def domainObject = classLoader.loadClass(className)
			
	def properties = []
	def fields = domainObject.declaredFields.findAll { !it.isSynthetic() && it.name != 'id' && it.type != Object }
		
	fields.each { field ->		
		String propertyName = field.name
		String label = propertyName[0].toUpperCase() + propertyName.substring(1).replaceAll(/([A-Z])/, / $1/)
		Class type = field.type
		boolean isDomainClass = type.name.startsWith(group)
		
		properties << [name: propertyName, label: label, type : type, domainClass: isDomainClass ]
	}
		
	properties
}

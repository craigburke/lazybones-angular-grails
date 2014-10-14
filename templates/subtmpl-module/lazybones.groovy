import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import java.lang.reflect.Modifier

def props = [:]

props.renderInput = { property, String modelPrefix ->
	if (property.domainClass) {
		"""
		<select name="${property.name}" class='form-control' ng-model="${modelPrefix}.${property.name}" ng-options="item.toText for item in ctrl.${property.name}List track by item.id" ></select>
		"""
	}
	else {
		String inputType = property.type in [Float, Integer] ? 'number' : 'text'
		"""
		<input name="${property.name}" type="${inputType}" class='form-control' ${property.type == Date ? 'date-field' : ''} ng-model="${modelPrefix}.${property.name}" />
		"""
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
			displayFilter = " | date: 'medium'"
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
 
props.formatResourceName = { String moduleName ->
	String resource = moduleName.tokenize('.').last()
	resource[0]?.toUpperCase() + resource?.substring(1)
}

boolean isCrudModule = (tmplQualifiers[1] != "blank")
props.group = parentParams.group
props.moduleName = props.formatModuleName(ask("Define the name for your new module [myModule]: ", "myModule", "moduleName"))

if (isCrudModule) {
	props.domainClassName = props.group + '.' + ask("Define the name of the domain class [Foo]: ", "Foo", "domainClass")
	props.domainProperties = getDomainProperties(props.domainClassName, props.group)
}

String moduleFilesDir = "angular/" + (isCrudModule ? "crud" : "blank")

props.rootModule = parentParams.angularModule
props.resourceName = props.formatResourceName(props.moduleName)
props.fullModuleName = "${parentParams.angularModule}.${props.moduleName}"
props.modulePath = props.getModulePath(props.fullModuleName)

def moduleLocation = new File("${projectDir}/grails-app/assets/javascripts/${props.modulePath}")
FileUtils.deleteQuietly(moduleLocation)

def copyAngularTemplates = {
	File source = new File(projectDir, "src/templates/angular/")	
	File destinatation = new File(templateDir, "angular/")	
	FileUtils.deleteQuietly(destinatation)
	FileUtils.copyDirectory(source, destinatation, true)
}

def generateController = {
	processTemplates "${moduleFilesDir}/Controller.groovy", props
	
    String groupPath = props.group.replace('.', '/') + '/'
	File source = new File(templateDir, "${moduleFilesDir}/Controller.groovy")
    File destination = new File(projectDir, "grails-app/controllers/${groupPath}/${props.resourceName}Controller.groovy")
	FileUtils.deleteQuietly(destination)
	
	FileUtils.moveFile(source, destination)
	
	//* Add custom JSON marshaller
	def customMarshallerRegistrar = new File(projectDir, "src/groovy/${groupPath}/CustomMarshallerRegistrar.groovy")
	String domainClass = "${props.group}.${props.resourceName}"
	String jsonMarshaller = 
	"""
		JSON.registerObjectMarshaller(${domainClass}) {
			def map = [:];
			map['id'] = it?.id;
			${props.domainProperties.collect { "map['" + it.name + "'] = it?." + it.name + ";" }.join('\n\t\t\t')}
	    	map['toText'] = it?.toString();
			return map 
		}"""
	// Remove existing marshaller
	def functionRegex = /(?ms)(JSON\.registerObjectMarshaller\((.*?)\).*?return.*?\})/
	customMarshallerRegistrar.text = customMarshallerRegistrar.text.replaceAll(functionRegex) { all, function, matchedDomainClass -> 
		(matchedDomainClass == domainClass) ? '' : all
	}
				
	// Add new marshaller
	customMarshallerRegistrar.text = customMarshallerRegistrar.text.replaceAll(/(?s)(registerMarshallers\(\).*?\{)/, "\$1\n${jsonMarshaller}")
}

def generateResourceUrlMapping = {
	def mappingFile = new File("${projectDir}/grails-app/conf/UrlMappings.groovy")
	
	String resourceMapping = "\t\t'/api/${props.moduleName}'(resources: '${props.moduleName}')\n"
	String viewMapping = "\t\t'/${props.moduleName}'(view: '${props.moduleName}')\n"
		
	if (!mappingFile.text.contains(resourceMapping)) {
		mappingFile.text = mappingFile.text.replaceAll(/(mappings\s*=\s*\{\s*\n*)/, "\$1${viewMapping}${resourceMapping}")
	}
}

def generatePage = {
	processTemplates "angular/common/index.gsp", props
	
	File source = new File(templateDir, "/angular/common/index.gsp")
	File destination = new File(projectDir, "grails-app/views/${props.moduleName}.gsp") 
	FileUtils.deleteQuietly(destination)
	
	FileUtils.moveFile(source, destination)
}

def generateModule = {
	if (isCrudModule) {
		props.defaultResource = "${props.resourceName}Resource"
		props.resourceUrl = "/api/${props.moduleName}"		
	}

	processTemplates "${moduleFilesDir}/javascript/**", props
	File source = new File(templateDir, "${moduleFilesDir}/javascript")

	FileUtils.moveDirectory(source, moduleLocation)
}


def generateTemplates = {
	processTemplates "${moduleFilesDir}/templates/**", props
	moduleLocation.mkdirs()
	
	File source = new File(templateDir, "${moduleFilesDir}/templates")
	FileUtils.moveDirectoryToDirectory(source, moduleLocation, true)
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
	generateController()	
	generateResourceUrlMapping()
}

generatePage()
generateModule()
generateTemplates()
printMessage()
cleanup()

def getDomainProperties(String className, String group) {
	def classLoader = new GroovyClassLoader()
	
	['grails-app/domain', 'grails-app/services', 'src/groovy', 'src/java'].each {
		classLoader.addClasspath(it)
	}
		
	def domainObject = classLoader.loadClass(className)
			
	def properties = []
	def fields = domainObject.declaredFields.findAll { !Modifier.isStatic(it.modifiers) && it.name != 'metaClass' }
		
	fields.each { field ->
		String propertyName = field.name
		String label = propertyName[0].toUpperCase() + propertyName.substring(1).replaceAll(/([A-Z])/, / $1/)
		Class type = field.genericType
		boolean isDomainClass = type.name.startsWith(group)
					
		properties << [name: propertyName, label: label, type : type, domainClass: isDomainClass ]
	}
		
	properties
}

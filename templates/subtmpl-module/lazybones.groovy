import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import java.lang.reflect.Modifier

def props = [:]

boolean isCrudModule = (tmplQualifiers[1] != "blank")
String moduleFilesDir = isCrudModule ? "crud" : "blank"

props.moduleName = formatModuleName(tmplQualifiers[0])
props.resourceName = getResourceName(props.moduleName)
props.group = parentParams.group
props.fullModuleName = "${parentParams.angularModule}.${props.moduleName}"
props.modulePath = getModulePath(props.fullModuleName)

if (isCrudModule) {
	props.domainProperties = getDomainProperties("${props.group}.${props.resourceName}", props.group)
}

def moduleLocation = new File("${projectDir}/grails-app/assets/javascripts/${props.modulePath}")
FileUtils.deleteQuietly(moduleLocation)

def generateResourceUrlMapping = {
	String resourceMapping = "\t\t'/api/${props.moduleName}'(resources: '${props.moduleName}')\n"
	def mappingFile = new File("${projectDir}/grails-app/conf/UrlMappings.groovy")
		
	if (!mappingFile.text.contains(resourceMapping)) {
		mappingFile.text = mappingFile.text.replaceAll(/(mappings\s*=\s*\{\s*\n*)/, "\$1${resourceMapping}")
	}
}

def generateTemplates = {
	processTemplates "${moduleFilesDir}/templates/**", props
	moduleLocation.mkdirs()
	
	File source = new File(templateDir, "${moduleFilesDir}/templates")
	FileUtils.moveDirectoryToDirectory(source, moduleLocation, true)
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

def generatePage = {
	processTemplates "common/index.gsp", props
	
	File source = new File(templateDir, "common/index.gsp")
	File destination = new File(projectDir, "grails-app/views/${props.moduleName}/index.gsp") 
	FileUtils.deleteQuietly(destination)
	
	FileUtils.moveFile(source, destination)
}

def generateController = {
	processTemplates "${moduleFilesDir}/Controller.groovy", props
	
    String groupPath = props.group.replace('.', '/') + '/'
	File source = new File(templateDir, "${moduleFilesDir}/Controller.groovy")
    File destination = new File(projectDir, "grails-app/controllers/${groupPath}/${props.resourceName}Controller.groovy")
	FileUtils.deleteQuietly(destination)
	
	FileUtils.moveFile(source, destination)
}

def printMessage = {
	println "Your Angular app (${props.fullModuleName}) has been created"
	println "URL: /${props.moduleName}"
}

if (isCrudModule) {
	generateController()	
	generateResourceUrlMapping()
}

generatePage()
generateModule()
generateTemplates()
printMessage()

def getDomainProperties(String clazz, String group) {
	def classLoader = new GroovyClassLoader()
	classLoader.addClasspath('grails-app/domain/')
		
	def domainObject = classLoader.loadClass(clazz)
			
	def properties = []
	def fields = domainObject.declaredFields.findAll { !Modifier.isStatic(it.modifiers) && it.name != 'metaClass' }
		
	fields.each { field ->
		String propertyName = field.name
		String label = propertyName[0].toUpperCase() + propertyName.substring(1).replaceAll(/([A-Z])/, / $1/)
		String type = field.genericType.name - 'java.lang.' - 'java.util.' - "${group}."
		
		String displayFilter = ""
		switch(type) {
			case "Integer":
				displayFilter = " | number"
				break
			case "Float":
				displayFilter = " | currency"
				break
			case "Date":
				displayFilter = " | date: 'medium'"
				break
		}		
					
		properties << [name: propertyName, label: label, type : type, displayFilter: displayFilter ]
	}
		
	properties
}
 
String formatModuleName(String moduleName) {
	def moduleParts = moduleName.tokenize('.')	
	moduleParts.collect { it[0]?.toLowerCase() + it?.substring(1) }.join('.')
} 
 
String getResourceName(String moduleName) {
	String resource = moduleName.tokenize('.').last()
	resource[0]?.toUpperCase() + resource?.substring(1)
}
 
String getModulePath(String moduleName) {
	String path = moduleName.replace('.', '/')
	path = path.replaceAll(/([A-Z])/,/-$1/).toLowerCase().replaceAll(/^-/,'')
	path.replaceAll(/\/-/, '/')
}

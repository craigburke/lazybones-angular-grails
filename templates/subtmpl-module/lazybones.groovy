import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import java.lang.reflect.Modifier

def props = [:]

boolean isRestModule = (tmplQualifiers[1] != "blank")
String moduleFilesDir = isRestModule ? "rest" : "blank"

props.moduleName = tmplQualifiers[0][0]?.toLowerCase() + tmplQualifiers[0]?.substring(1)
props.resourceName = props.moduleName[0]?.toUpperCase() + props.moduleName?.substring(1)
props.group = parentParams.group
props.fullModuleName = "${parentParams.angularModule}.${props.moduleName}"
props.modulePath = getModulePath(props.fullModuleName)

if (isRestModule) {
	props.domainProperties = getDomainProperties("${props.group}.${props.resourceName}", props.group)
}

def moduleLocation = new File("${projectDir}/grails-app/assets/javascripts/${props.modulePath}")
FileUtils.deleteQuietly(moduleLocation)

def generateUrlMappings = {
	String pageMapping = "'/${props.moduleName}'(view: '${props.moduleName}')"
	String resourceMapping = "'/api/${props.moduleName}'(resources: '${props.moduleName}')"
	String indent = "\t" * 2
	String urlMapping = "\n${indent}${pageMapping}\n${indent}${resourceMapping}\n\n${indent}"

	def mappingFile = new File("${projectDir}/grails-app/conf/UrlMappings.groovy")
		
	if (!mappingFile.text.contains(urlMapping)) {
		mappingFile.text = mappingFile.text.replaceAll(/(mappings\s*=\s*\{\s*\n*)/, "\$1${urlMapping}")
	}
}

def generateTemplates = {
	processTemplates "${moduleFilesDir}/templates/**", props
	moduleLocation.mkdirs()
	
	File source = new File(templateDir, "${moduleFilesDir}/templates")
	FileUtils.moveDirectoryToDirectory(source, moduleLocation, true)
}

def generateModule = {
	if (isRestModule) {
		props.defaultResource = "${props.resourceName}Resource"
		props.resourceUrl = "/api/${props.moduleName}"		
	}

	processTemplates "${moduleFilesDir}/javascript/**", props
	File source = new File(templateDir, "${moduleFilesDir}/javascript")

	FileUtils.moveDirectory(source, moduleLocation)
}

def generatePage = {
	processTemplates "common/page.gsp", props
	
	File source = new File(templateDir, "common/page.gsp")
	File destination = new File(projectDir, "grails-app/views/${props.moduleName}.gsp") 
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

if (isRestModule) {
	generateController()	
}

generatePage()
generateUrlMappings()
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
		String simpleType = field.genericType.name - ['java.lang.', 'java.util.', "${group}."]
					
		properties << [name: propertyName, label: label, type : simpleType ]
	}
		
	properties
}
 
String getModulePath(String moduleName) {
	String path = moduleName.replace('.', '/')
	path = path.replaceAll(/([A-Z])/,/-$1/).toLowerCase().replaceAll(/^-/,'')
	path.replaceAll(/\/-/, '/')
}

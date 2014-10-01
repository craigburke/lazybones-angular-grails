import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import java.lang.reflect.Modifier

def props = [:]
props.moduleName = tmplQualifiers[0][0]?.toLowerCase() + tmplQualifiers[0]?.substring(1)
props.resourceName = props.moduleName[0]?.toUpperCase() + props.moduleName?.substring(1)
props.group = parentParams.group
props.fullModuleName = "${parentParams.angularModule}.${props.moduleName}"
props.modulePath = getModulePath(props.fullModuleName)
props.domainProperties = getDomainProperties("${props.group}.${props.resourceName}", props.group)

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
	processTemplates "templates/**", props
	moduleLocation.mkdirs()
	
	File source = new File(templateDir, "templates")
	FileUtils.moveDirectoryToDirectory(source, moduleLocation, true)
}

def generateModule = {
	props.defaultResource = "${props.resourceName}Resource"
	props.resourceUrl = "/api/${props.moduleName}"	

	processTemplates "javascript/**", props
	File source = new File(templateDir, "javascript")

	FileUtils.moveDirectory(source, moduleLocation)
}

def generatePage = {
	processTemplates "page.gsp", props
	
	File source = new File(templateDir, "page.gsp")
	File destination = new File(projectDir, "grails-app/views/${props.moduleName}.gsp") 
	FileUtils.deleteQuietly(destination)
	
	FileUtils.moveFile(source, destination)
}

def generateController = {
	processTemplates "Controller.groovy", props
	
    String groupPath = props.group.replace('.', '/') + '/'
	File source = new File(templateDir, "Controller.groovy")
    File destination = new File(projectDir, "grails-app/controllers/${groupPath}/${props.resourceName}Controller.groovy")
	FileUtils.deleteQuietly(destination)
	
	FileUtils.moveFile(source, destination)
}

generateController()
generatePage()
generateUrlMappings()
generateModule()
generateTemplates()

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

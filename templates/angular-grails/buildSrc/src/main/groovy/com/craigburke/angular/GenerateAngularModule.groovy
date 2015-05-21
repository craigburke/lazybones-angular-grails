package com.craigburke.angular

import java.lang.reflect.Field
import static groovy.io.FileType.FILES
import static java.lang.reflect.Modifier.isStatic
import groovy.text.SimpleTemplateEngine

class GenerateAngularModule {

	static String projectPath
	
 	public static void main(String[] args) {
		def props = [:]
	
		props.group = args[0]
		props.groupPath = props.group.tokenize('.').join('/')
		props.domainClassName = args[3].contains('.') ? args[3] : "${props.group}.${args[3]}"
	    props.domainProperties = getDomainProperties(props.group, props.domainClassName)
		
		props.rootModule = args[1]
		props.moduleName = args[2]
		props.fullModuleName = "${props.rootModule}.${props.moduleName}"
		props.modulePath = getModulePath(props.fullModuleName)
		
		props.resourceName = getResourceName(props.fullModuleName)
	    props.defaultResource = "${props.resourceName}Resource"
	    props.resourceUrl = "/api/${props.moduleName}"
		
		props.formatModuleName = { String moduleName ->
		    def moduleParts = moduleName.tokenize('.')
		    moduleParts.collect { it[0]?.toLowerCase() + it?.substring(1) }.join('.')
		}
		
		props.getModulePath = { String fullModule ->
    		String path = fullModule.replace('.', '/')
    		path = path.replaceAll(/([A-Z])/, /-$1/).toLowerCase().replaceAll(/^-/, '')
    		path.replaceAll(/\/-/, '/')
		}
		
		projectPath = args[4]
		
		def renderUtilFile = new File("${projectPath}/src/templates/angular/RenderUtil.groovy")
		if (renderUtilFile.exists()) {
		    props << new GroovyShell().evaluate(renderUtilFile)
		}
		
		processTemplateFiles(props)
		updateUrlMappings(props.moduleName)
 	}
	
	private static String getModulePath(String moduleName) {
		String path = moduleName.replace('.', '/')
	 	path = path.replaceAll(/([A-Z])/, /-$1/).toLowerCase().replaceAll(/^-/, '')
    	path.replaceAll(/\/-/, '/')
	}
	
	private static String getResourceName(String moduleName) {
	    String resource = moduleName.tokenize('.').last()
	    resource[0]?.toUpperCase() + resource?.substring(1)
	}
	
	private static getDomainProperties(String group, String fullClassName) {
		Class domainClass
    	try {
			def classLoader = new GroovyClassLoader()
        	domainClass = classLoader.loadClass(fullClassName)
    	} catch (ex) {
        	throw new Exception("Unable to load domain class: ${fullClassName}", ex)
    	}

		def constraints = [:]
		if (domainClass.constraints) {
			def constraintsClosure = domainClass.constraints.clone()
			Expando constraintDelegate = new Expando()
			constraintDelegate.metaClass.methodMissing = { String name, args ->
		        constraints[name] = [required: true, nullable: false]
				constraints[name].putAll(args[0])
			}
			constraintsClosure.delegate = constraintDelegate
			constraintsClosure()
		}

		def ignoreFields = ['metaClass', 'class', 'attached', 'dirty', 'dirtyPropertyNames',
			'properties', 'errors']
		
    	domainClass.declaredFields
			.findAll { !(it.name in ignoreFields) }
			.collect { [
				name: it.name, 
				label: it.name[0].toUpperCase() + it.name.substring(1).replaceAll(/([A-Z])/, / $1/),
				type: it.type, 
				domainClass: it.type.name.startsWith(group),
				constraints: constraints[it.name] ?: [:]
			] }
	}
	
	
	private static void processTemplateFiles(props) {
	    File templateDirectory = new File(projectPath, "/src/templates/angular/crud")
	    File commonDirectory = new File(projectPath, "/src/templates/angular/common")
		
		[templateDirectory, commonDirectory].each { File directory ->
			directory.eachFileRecurse(FILES) { File file ->
				File destinationPath = getDestinationPath(directory, file, props)
				processFile(file, destinationPath, props)
			}
		}
	}
	
	private static File getDestinationPath(File templateFolder, File file, props) {
		String relativePath = file.absolutePath - templateFolder.absolutePath
		def pathVariables = ['groupPath', 'resourceName', 'moduleName', 'modulePath']

        props.findAll { it.key in pathVariables }.each { key, value ->
            relativePath = relativePath.replace("_${key}_", value)
        }
		new File("${projectPath}/${relativePath}")
	}
	
	private static void processFile(File source, File destination, props) {
		if (destination.exists()) {
			destination.delete()
		}
		new File(destination.parent).mkdirs()
		
		def engine = new SimpleTemplateEngine()
		def template = engine.createTemplate(source).make(props)	
		destination << template.toString()
	}
	
	private static void updateUrlMappings(String moduleName) {
    	def mappingFile = new File("${projectPath}/grails-app/controllers/UrlMappings.groovy")

    	String mapping = "\t\t'/${moduleName}'(view:'/${moduleName}')\n"
        // Add resource mapping
        mapping += "\t\t'/api/${moduleName}'(resources:'${moduleName}')\n"

    	if (!mappingFile.text.contains(mapping)) {
        	mappingFile.text = mappingFile.text.replaceAll(/(mappings\s*=\s*\{\s*\n*)/, "\$1${mapping}")
    	}
	}
}
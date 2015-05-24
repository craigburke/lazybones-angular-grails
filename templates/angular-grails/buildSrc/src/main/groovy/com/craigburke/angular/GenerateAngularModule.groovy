package com.craigburke.angular

import org.grails.datastore.mapping.reflect.ClassPropertyFetcher
import static groovy.io.FileType.FILES
import groovy.text.SimpleTemplateEngine

class GenerateAngularModule {

	static String projectPath
	static Boolean crudModule
	
 	public static void main(String[] args) {
		def props = [:]
	
		projectPath = args[0]
		props.group = args[1]
		props.groupPath = props.group.tokenize('.').join('/')
	
		props.rootModule = args[2]
		props.moduleName = args[3]
		props.fullModuleName = "${props.rootModule}.${props.moduleName}"
		props.modulePath = getModulePath(props.fullModuleName)
				
		crudModule = args[4]
		if (crudModule) {
			boolean fullClassSpecified = args[4].contains('.')
			
			if (fullClassSpecified) {
				props.domainClassName = args[4]
				props.domainClassGroup = props.domainClassName.tokenize('.')[0..-2].join('.')
			}
			else {
				props.domainClassName = "${props.group}.${args[4]}"
				props.domainClassGroup = props.group
			}
			
			props.domainClassGroupPath = props.domainClassGroup.tokenize('.').join('/')

		    props.domainProperties = getDomainProperties(props.group, props.domainClassName)
			props.resourceName = getResourceName(props.domainClassName)
		    props.angularResource = "${props.resourceName}Resource"
		    props.angularResourceUrl = "/api/${props.moduleName}"
		}
		
		props.formatModuleName = { String moduleName ->
		    def moduleParts = moduleName.tokenize('.')
		    moduleParts.collect { it[0]?.toLowerCase() + it?.substring(1) }.join('.')
		}
		
		props.getModulePath = { String fullModule ->
    		String path = fullModule.replace('.', '/')
    		path = path.replaceAll(/([A-Z])/, /-$1/).toLowerCase().replaceAll(/^-/, '')
    		path.replaceAll(/\/-/, '/')
		}
		
		def renderUtilFile = new File("${projectPath}/src/templates/angular/RenderUtil.groovy")
		if (renderUtilFile.exists()) {
		    props << new GroovyShell().evaluate(renderUtilFile)
		}
		
		processTemplateFiles(props)
		createCustomMarshaller(props)
		updateUrlMappings(props)
 	}
	
	private static void createCustomMarshaller(props) {
	       File customMarshallerRegistrar = new File(projectPath, "src/main/groovy/${props.groupPath}/CustomMarshallerRegistrar.groovy")

	       String jsonMarshaller =
	        """
	   		JSON.registerObjectMarshaller(${props.domainClassName}) {
	   			def map = [:]
	   			map['id'] = it?.id
	   			${props.domainProperties.collect { "map['" + it.name + "'] = it?." + it.name }.join('\n\t\t\t')}
	   	    	map['toText'] = it?.toString()
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
	
	private static String getModulePath(String moduleName) {
		String path = moduleName.replace('.', '/')
	 	path = path.replaceAll(/([A-Z])/, /-$1/).toLowerCase().replaceAll(/^-/, '')
    	path.replaceAll(/\/-/, '/')
	}
	
	private static String getResourceName(String domainClassName) {
	    String resource = domainClassName.tokenize('.').last()
	    resource[0]?.toUpperCase() + resource?.substring(1)
	}
	
	private static getDomainProperties(String group, String domainClassName) {
		Class domainClass
    	try {
			def classLoader = new GroovyClassLoader()
        	domainClass = classLoader.loadClass(domainClassName)
    	} catch (ex) {
        	throw new Exception("Unable to load domain class: ${domainClassName}", ex)
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

		ClassPropertyFetcher propertyFetcher = new ClassPropertyFetcher(domainClass)
		def ignoreFields = ['id', 'version', 'metaClass', 'class', 'attached', 'dirty', 'dirtyPropertyNames',
					'properties', 'errors']
					
    	propertyFetcher.propertyDescriptors
			.findAll { !(it.name in ignoreFields) }
			.collect { [
				name: it.name, 
				label: it.name[0].toUpperCase() + it.name.substring(1).replaceAll(/([A-Z])/, / $1/),
				type: it.propertyType, 
				domainClass: it.propertyType.name.startsWith(group),
				constraints: constraints[it.name] ?: [:]
			] }
	}
	
	
	private static void processTemplateFiles(props) {
	    File templateDirectory = new File(projectPath, "/src/templates/angular/${crudModule ? 'crud' : 'blank'}")
	    File commonDirectory = new File(projectPath, '/src/templates/angular/common')
		
		[templateDirectory, commonDirectory].each { File directory ->
			directory.eachFileRecurse(FILES) { File file ->
				File destinationPath = getDestinationPath(directory, file, props)
				processFile(file, destinationPath, props)
			}
		}
	}
	
	private static File getDestinationPath(File templateFolder, File file, props) {
		String relativePath = file.absolutePath - templateFolder.absolutePath
		def pathVariables = ['groupPath', 'domainClassGroupPath', 'resourceName', 'moduleName', 'modulePath']

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
	
	private static void updateUrlMappings(props) {
		String resourceName = props.domainClassName.tokenize('.').last()
		resourceName = resourceName[0]?.toLowerCase() + resourceName?.substring(1)
		
    	def mappingFile = new File("${projectPath}/grails-app/controllers/UrlMappings.groovy")

    	String mapping = "\t\t'/${resourceName}'(view:'/${resourceName}')\n"
        // Add resource mapping
        mapping += "\t\t'${props.angularResourceUrl}'(resources:'${resourceName}')\n"

    	if (!mappingFile.text.contains(mapping)) {
        	mappingFile.text = mappingFile.text.replaceAll(/(mappings\s*=\s*\{\s*\n*)/, "\$1${mapping}")
    	}
	}
}
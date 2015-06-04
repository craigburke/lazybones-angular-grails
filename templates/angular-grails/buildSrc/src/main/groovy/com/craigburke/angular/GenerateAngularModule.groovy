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
				props.domainClassFullName = args[4]
				props.domainClassGroup = props.domainClassName.tokenize('.')[0..-2].join('.')
			}
			else {
				props.domainClassFullName = "${props.group}.${args[4]}"
				props.domainClassGroup = props.group
			}
			props.domainClassName = props.domainClassFullName.tokenize('.').last()
			props.domainClassNameLowerCase = props.domainClassName[0].toLowerCase() + props.domainClassName.substring(1)
			props.domainClassGroupPath = props.domainClassGroup.tokenize('.').join('/')

		    props.domainProperties = getDomainProperties(props.group, props.domainClassFullName)
		    props.angularResource = "${props.domainClassName}Resource"
		    props.angularResourceUrl = "/api/${props.domainClassNameLowerCase}"
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
		addModuleToApplication(props)
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
	
	private static getDomainProperties(String group, String domainClassFullName) {
		Class domainClass
    	try {
			def classLoader = new GroovyClassLoader()
        	domainClass = classLoader.loadClass(domainClassFullName)
    	} catch (ex) {
        	throw new Exception("Unable to load domain class: ${domainClassFullName}", ex)
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
			.collect { 
				boolean isHasMany = Collection.isAssignableFrom(it.propertyType) && domainClass.hasMany[it.name]
				Class propertyType = isHasMany ? domainClass.hasMany[it.name] : it.propertyType
				boolean isDomainClass = (propertyType.name.startsWith(group) || isHasMany)
				String label = it.name[0].toUpperCase() + it.name.substring(1).replaceAll(/([A-Z])/, / $1/)
				String classNameLowerCase = propertyType.name.tokenize('.').last()
				classNameLowerCase = classNameLowerCase[0].toLowerCase() + classNameLowerCase.substring(1)
				
				[ 	
					name: it.name,
					label: label,
					type: propertyType,
					classNameLowerCase: classNameLowerCase,
					isDomainClass: isDomainClass,
					isHasMany: isHasMany,
					constraints: constraints[it.name] ?: [:]
				] 
			}
			.sort { field ->
				constraints.findIndexOf { it.key == field.name }
			}
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
		def pathVariables = ['groupPath', 'domainClassGroupPath', 'domainClassName', 'moduleName', 'modulePath']

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
    	def mappingFile = new File("${projectPath}/grails-app/controllers/UrlMappings.groovy")

    	String mapping = "\t\t'${props.angularResourceUrl}'(resources:'${props.domainClassNameLowerCase}')\n"

    	if (!mappingFile.text.contains(mapping)) {
        	mappingFile.text = mappingFile.text.replaceAll(/(mappings\s*=\s*\{\s*\n*)/, "\$1${mapping}")
		}
	}	
	
	private static void addModuleToApplication(props) {
		File applicationJsFile = new File("${projectPath}/grails-app/assets/javascripts/application.js")
		String moduleAssetDirective = "//= require ${props.modulePath}/module"
		if (!applicationJsFile.text.contains(moduleAssetDirective)) {
			def lines = applicationJsFile.readLines()
			applicationJsFile.text =  "${lines.first()}\n${moduleAssetDirective}\n${lines.tail().join('\n')}" 
		}
		
		String moduleDependency = "'${props.fullModuleName}'"
		if (!applicationJsFile.text.contains(moduleDependency)) {
			String originalModuleDefinition = (applicationJsFile.text =~ /(?s).*(angular\.module.*)/)[0][1]
			String originalDependencyList = (originalModuleDefinition =~ /(?s).*\[(.*)\]/)[0][1]
			String seperator = (originalDependencyList.trim()) ? ',' : ''
			String newDependencyList = "\n\t${moduleDependency}${seperator}${originalDependencyList}"
			applicationJsFile.text = applicationJsFile.text.replace(originalDependencyList, newDependencyList)
		}

	}
}
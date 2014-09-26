package com.craigburke.angular.grails

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.lang.reflect.Modifier

class AngularGenerateTask extends DefaultTask {	
	private String fullModule
	private String modulePath
	private String resourceName
	private String jsRoot
	private String moduleName
	private def domainProperties

	String baseModule
	String domainClass
	String group

	@TaskAction
	void generate() {
		moduleName = getModuleName(domainClass)
		fullModule = "${baseModule}.${moduleName}"
		modulePath = findModulePath(fullModule)
        resourceName = moduleName[0].toUpperCase() + moduleName.substring(1)
        jsRoot = "grails-app/assets/javascripts"
		
		domainProperties = getDomainProperties()
		createModule()
		createTemplates()		
		createApp()
		setUrlMappings()
	}
	
	private def getDomainProperties() {
		def classLoader = new GroovyClassLoader()
		classLoader.addClasspath('grails-app/domain/')
		
		def domainObject = classLoader.loadClass("${group}.${domainClass}")
			
		def properties = []
		def fields = domainObject.declaredFields.findAll { !Modifier.isStatic(it.modifiers) && it.name != 'metaClass' }
		
		fields.each { field ->
			String propertyName = field.name
			String label = propertyName[0].toUpperCase() + propertyName.substring(1).replaceAll(/([A-Z])/, / $1/)
			String simpleType = field.genericType.name - 'java.lang.' - 'java.util.' - "${group}."
					
			properties << [name: propertyName, label: label, type : simpleType ]
		}
		
		properties
	}


	private String findModulePath(module) {
		String path = module.replace('.', '/')
		path = path.replaceAll(/([A-Z])/,/-$1/).toLowerCase().replaceAll(/^-/,'')
		path.replaceAll(/\/-/, '/')
	}

	private String getModuleName(domainClass) {
		domainClass ? domainClass[0].toLowerCase() + domainClass.substring(1) : ''
	}

	def createModule() {
		String defaultResource = "${resourceName}Resource"
		String resourceUrl = "/api/${moduleName}"	
		String destination = "${jsRoot}/${modulePath}"

		def props = [
			resourceName: resourceName,
			defaultResource: defaultResource,
            moduleName: fullModule,
            modulePath: modulePath,
            baseModule: baseModule,
            resourceUrl: resourceUrl
		]

		project.copy {
			from 'src/templates/angular/javascript'
			include '*.js'
			into destination
			expand(	props )
		}
	}

	def createTemplates() {
        String destination = "${jsRoot}/${modulePath}/templates"
        def props = [resourceName: resourceName, maxListItems: 4, domainProperties: domainProperties]

		project.copy {
  			from 'src/templates/angular/templates'
            include '*.*'
            into destination
            expand (props)
		}
	}

	def createApp() {
		def viewProps = [fullModule: fullModule, modulePath: modulePath]
		String fileName = "${moduleName}.gsp"

		project.copy {
			from 'src/templates/angular/page.gsp'
			into 'grails-app/views/'
			rename { fileName }
			expand ( viewProps )
		}

        String groupPath = group.replace('.', '/') + '/'
        String controllerDestination = "grails-app/controllers/${groupPath}/"
        String controllerFileName = "${resourceName}Controller.groovy"

        def controllerProps = [group: group, resourceName: resourceName]

		project.copy {
			from 'src/templates/angular/Controller.groovy'
			into controllerDestination
            rename { controllerFileName }
            expand ( controllerProps )
		}
	}

	def setUrlMappings() {
		String pageMapping = "'/${moduleName}'(view: '${moduleName}')"
		String resourceMapping = "'/api/${moduleName}'(resources: '${moduleName}')"
		String indent = "\t" * 2
		String urlMapping = "\n${indent}${pageMapping}\n${indent}${resourceMapping}\n\n${indent}"

		def mappingFile = project.file('grails-app/conf/UrlMappings.groovy')
		
		if (!mappingFile.text.contains(urlMapping)) {
            mappingFile.text = mappingFile.text.replaceAll(/(mappings\s*=\s*\{\s*\n*)/, "\$1${urlMapping}")
		}
	}
}

package com.craigburke.angular.grails

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class AngularGenerateTask extends DefaultTask {	
	private String fullModule
	private String modulePath
	private String resourceName
	private String jsRoot

	String baseModule
	String moduleName
	String group

	@TaskAction
	void generate() {
		moduleName = formatModuleName(moduleName)
		fullModule = "${baseModule}.${moduleName}"
		modulePath = findModulePath(fullModule)
        resourceName = moduleName[0].toUpperCase() + moduleName.substring(1)

        jsRoot = "grails-app/assets/javascripts"

		createModule()
		createTemplates()		
		createApp()
		setUrlMappings()
	}

	private String findModulePath(module) {
		String path = module.replace('.', '/')
		path = path.replaceAll(/([A-Z])/,/-$1/).toLowerCase().replaceAll(/^-/,'')
		path.replaceAll(/\/-/, '/') + '/'
	}

	private String formatModuleName(module) {
		module ? module[0].toLowerCase() + module.substring(1) : ''
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
        String destination = "${jsRoot}/templates/${modulePath}"
        def props = [resourceName: resourceName]

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

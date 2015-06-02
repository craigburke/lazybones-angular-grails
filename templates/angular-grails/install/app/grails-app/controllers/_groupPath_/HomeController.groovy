package ${group}

import grails.converters.JSON

class HomeController {
				
	def info() {
		
		def info = [
			environment:grails.util.Environment.current.name,
			profile:grailsApplication.config.grails?.profile,
			applicationVersion:grailsApplication.metadata.getApplicationVersion(),
			grailsVersion:grailsApplication.metadata.getGrailsVersion(),
			groovyVersion:GroovySystem.version,
			jvmVersion:System.getProperty('java.version'),
			reloadingActive:grails.util.Environment.reloadingAgentEnabled,
			controllerCount:grailsApplication.controllerClasses.size(),
			domainCount:grailsApplication.domainClasses.size(),
			serviceCount:grailsApplication.serviceClasses.size(),
			tagLibraryCount:grailsApplication.tagLibClasses.size()
		]
		
		info.plugins = applicationContext.getBean('pluginManager').allPlugins.collect { [ name:it.name, version:it.version] }
		
		render info as JSON
	}
}
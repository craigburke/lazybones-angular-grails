package com.craigburke.angular

import org.gradle.api.Plugin
import org.gradle.api.Project

class AngularModulePlugin implements Plugin<Project> {
     void apply(Project project) {	 
       project.extensions.create('angular', AngularModuleExtension)
		
       project.tasks.create(name:'generateAngularModule', dependsOn:'classes') << {
		   project.javaexec {
			   classpath = project.files("${project.rootDir}/buildSrc/build/classes/main")
			   classpath += project.files("${project.rootDir}/build/classes/main")
			   classpath += project.sourceSets.main.runtimeClasspath
			   main = 'com.craigburke.angular.GenerateAngularModule'
			   args = [
					"${project.rootDir}",
			   		project.group,
					project.angular.baseModule,
					project.angular.moduleName,
					project.angular.domainClass
			   ]
		   }
        }
    }
}
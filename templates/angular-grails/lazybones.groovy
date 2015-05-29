import org.apache.commons.io.FileUtils
import static groovy.io.FileType.FILES

def params = [:]

String installDirName = "install"
File installDir = new File(templateDir, installDirName)

params.appName = ask('Define the name for your project [angular-grails]: ', 'angular-grails', 'appName')
params.angularVersion = ask('Define the version of AngularJS you want in your project [1.3]: ', '1.3', 'angularVersion')
params.baseModule = ask('Define value for your main AngularJS module [myApp]: ', 'myApp', 'baseModule')
params.group = ask('Define the value for your application group [com.company]: ', 'com.company', 'group')
params.version = ask('Define value for your application \'version\' [0.1]: ', '0.1', 'version')
params.archiveName = ask('Define the name of your archive files (JAR and WAR) [ROOT]: ', 'ROOT', 'archiveName')

params.modulePath = { String moduleName ->
	String path = moduleName.replace('.', '/')
 	path = path.replaceAll(/([A-Z])/, /-$1/).toLowerCase().replaceAll(/^-/, '')
	path.replaceAll(/\/-/, '/')
}(params.baseModule)

processTemplates 'build.gradle', params
processTemplates 'gradle.properties', params
processTemplates "${installDirName}/app/**/*", params
processTemplates 'grails-app/assets/javascripts/application.js', params

def processFile = { File baseDirectory, File file ->
	String relativePath = file.path - baseDirectory.path
	String groupPath = params.group.replace('.', '/')		
	String destinationPath = relativePath.replace("_groupPath_", groupPath)
	destinationPath = destinationPath.replace("_modulePath_", params.modulePath)
		
	File destination = new File(templateDir, destinationPath)
	FileUtils.copyFile(file, destination)
}

File appDirectory = new File(installDir, "app")	
appDirectory.eachFileRecurse(FILES) { processFile(appDirectory, it) }

FileUtils.copyDirectory new File(installDir, "angular/${params.angularVersion}"), templateDir
FileUtils.deleteDirectory(installDir)

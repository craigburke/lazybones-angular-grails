import org.apache.commons.io.FileUtils

def params = [:]

String installDirName = "install"
File installDir = new File(templateDir, installDirName)

params.appName = ask("Define the name for your project [angular-grails]: ", "angular-grails", "appName")
params.angularVersion = ask("Defined the version of AngularJS you want in your project [1.3]: ", "1.3", "angularVersion")
params.angularModule = ask("Define value for your main AngularJS module [myApp]: ", "myApp", "angularModule")
params.group = ask("Define the value for your application group [com.company]: ", "com.company", "group")
params.version = ask("Define value for your application 'version' [0.1]: ", "0.1", "version")
params.warName = ask("Define the name for your war file [ROOT.war]: ", "ROOT.war", "warName")

processTemplates 'gradle.properties', params
processTemplates 'application.properties', params
processTemplates 'grails-app/conf/Config.groovy', params
processTemplates "${installDirName}/*", params

String groupPath = params.group.replace('.', '/')

FileUtils.moveFile new File(installDir, 'PagedRestfulController.groovy'), new File(templateDir, "src/groovy/${groupPath}/PagedRestfulController.groovy")
FileUtils.moveFile new File(installDir, 'CustomMarshallerRegistrar.groovy'), new File(templateDir, "src/groovy/${groupPath}/CustomMarshallerRegistrar.groovy")
FileUtils.moveFile new File(installDir, 'resources.groovy'), new File(templateDir, "grails-app/conf/spring/resources.groovy")
FileUtils.copyDirectory new File(installDir, "angular/${params.angularVersion}"), templateDir

FileUtils.deleteDirectory(installDir)

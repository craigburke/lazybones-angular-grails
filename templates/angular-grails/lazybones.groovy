import org.apache.commons.io.FileUtils

def params = [:]

String installDirName = "install"
File installDir = new File(templateDir, installDirName)

params.group = ask("Define the value for your application group [com.company]: ", "com.company", "group")
params.version = ask("Define value for your application 'version' [0.1]: ", "0.1", "version")
params.angularModule = ask("Define value for your main AngularJS module [myApp]: ", "myApp", "angularModule")

processTemplates 'gradle.properties', params
processTemplates 'grails-app/conf/Config.groovy', params
processTemplates "${installDirName}/AngularController.groovy", params

String groupPath = params.group.replace('.', '/')

FileUtils.moveFile new File(installDir, 'AngularController.groovy'), new File(templateDir, "src/groovy/${groupPath}/AngularController.groovy")

FileUtils.deleteDirectory(installDir)

import org.apache.commons.io.FileUtils

def params = [:]

String tempDirName = "temp"
File tempDir = new File(templateDir, tempDirName)

params.group = ask("Define the value for your application group [com.company]: ", "com.company", "group")
params.version = ask("Define value for your application 'version' [0.1]: ", "0.1", "version")
params.angularModule = ask("Define value for your main AngularJS module [myApp]: ", "myApp", "angularModule")

processTemplates 'gradle.properties', params
processTemplates 'grails-app/conf/Config.groovy', params
processTemplates "${tempDirName}/AngularController.groovy", params

String groupPath = params.group.replace('.', '/')

FileUtils.moveFile new File(tempDir, 'AngularController.groovy'), new File(templateDir, "src/groovy/${groupPath}/AngularController.groovy")

FileUtils.deleteDirectory(tempDir)

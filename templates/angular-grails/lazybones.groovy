def props = [:]

props.group = ask("Define the value for your application group [com.company]: ", "com.company", "group")
props.version = ask("Define value for your application 'version' [0.1]: ", "0.1", "version")
props.angularModule = ask("Define value for your main AngularJS module [myApp]: ", "myApp", "angularModule")

processTemplates 'gradle.properties', props

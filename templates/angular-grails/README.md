# Introduction

You have created an Angular Grails application using lazybones.

For more detailed information see:
https://github.com/craigburke/lazybones-angular-grails

# Running the application
		gradlew run

# Running tests
		gradlew test
		
# Generate scaffolding

	* Create a domain class
		gradlew create-domain-class -PgrailsArgs=Foo

	* Generate your angular module
		lazybones generate module::Foo


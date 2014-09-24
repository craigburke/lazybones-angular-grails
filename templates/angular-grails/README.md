# Introduction

You have created an Angular Grails application using lazybones.

For more detailed information see:
https://github.com/craigburke/lazybones-angular-grails

This project uses the grails gradle plugin and includes several gradle tasks to help get you started.

# Running the application
		./gradlew run

# Running tests
* To run all tests
		./gradlew test

* To run Jasmine tests
		./gradlew jasmineRun

* To run Jasmine tests (in watch mode)
		./gradlew jasmineWatch


# Generate scaffolding
What follows are the steps to scaffold a new angular CRUD module:
* Create a domain class
	./gradlew create-domain-class -PgrailsArgs=Foo

* Generate the angular module
	./gradlew ngGenerate -PngModule=Foo


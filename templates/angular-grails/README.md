# Introduction
You have just created an Angular Grails application using lazybones.

For more detailed information see:
https://github.com/craigburke/lazybones-angular-grails

# Running the application
	gradlew run

# Running tests
	gradlew test

# Generate blank application
        lazybones generate module::blank

# Generate REST application
	* Create a domain class:
	gradlew grails-create-domain-class -PgrailsArgs=Foo

	* Generate your angular module:
	lazybones generate module
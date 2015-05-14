#/bin/sh
export PROJECT_ROOT=~/clone
export APP_ROOT="$PROJECT_ROOT/test/app"

# Build Angular 1.3
cd $PROJECT_ROOT
./gradlew buildTestApp -PangularVersion=1.3
cd $APP_ROOT
./gradlew assemble
mv build/libs/*.jar build/libs/angular-grails.jar
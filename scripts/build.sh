#/bin/sh
export PROJECT_ROOT=~/clone
export APP_ROOT="$PROJECT_ROOT/test/app"

# Build
cd $APP_ROOT
./gradlew assemble
mv build/libs/*.jar build/libs/angular-grails.jar
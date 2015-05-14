#/bin/sh
source ./common.sh

install_lazybones

# Build Angular 1.3
cd $PROJECT_ROOT
./gradlew buildTestApp -PangularVersion=1.3
cd $APP_ROOT
./gradlew assemble
mv build/libs/app-1.0.jar build/libs/angular-grails.jar

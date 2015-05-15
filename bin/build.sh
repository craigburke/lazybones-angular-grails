#/bin/sh
source ./common.sh

install_lazybones

cd $PROJECT_ROOT
./gradlew buildTestApps

cd $APP_ROOT
./gradlew assemble
mv build/libs/app-1.0.jar build/libs/angular-grails.jar
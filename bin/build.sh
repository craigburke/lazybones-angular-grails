#/bin/sh
source ./common.sh

install_lazybones

cd $PROJECT_ROOT
./gradlew buildTestApps

cd $APP_ROOT
./gradlew assemble
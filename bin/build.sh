#/bin/sh
source ./common.sh

install_lazybones

cd $PROJECT_ROOT
./gradlew buildTestApps

cd $DEPLOY_APP_ROOT
./gradlew assemble
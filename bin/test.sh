#/bin/sh
source ./common.sh

install_lazybones

cd $PROJECT_ROOT
./gradlew buildTestApps

# Test Angular 1.2
cd $PROJECT_ROOT/test/app/1.2/
./gradlew test

# Test Angular 1.3
cd $PROJECT_ROOT/test/app/1.3/
./gradlew test

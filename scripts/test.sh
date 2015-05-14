#/bin/sh
source ./common.sh

install_lazybones

# Test Angular 1.2
cd $PROJECT_ROOT
./gradlew buildTestApp -PangularVersion=1.2
cd $APP_ROOT
./gradlew test

# Test Angular 1.3
cd $PROJECT_ROOT
./gradlew buildTestApp -PangularVersion=1.3
cd $APP_ROOT
./gradlew test

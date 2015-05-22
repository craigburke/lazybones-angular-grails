#/bin/sh
source ./common.sh

install_lazybones

if [ -d "$TEST_APP_ROOT" ]; then
	cd $PROJECT_ROOT
	./gradlew buildTestApps
fi

# Test Angular 1.2
cd $TEST_APP_ROOT/1.2
./gradlew test

# Test Angular 1.3
cd $TEST_APP_ROOT/1.3
./gradlew test

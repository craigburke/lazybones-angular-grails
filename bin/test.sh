#/bin/sh
source ./common.sh

install_lazybones
cd $PROJECT_ROOT

if [ ! -d "$TEST_APP_ROOT" ]; then
	./gradlew buildTestApps
fi

# Test Angular 1.2
cd $TEST_APP_ROOT/1.2
./gradlew test

# Test Angular 1.3
cd $TEST_APP_ROOT/1.3
./gradlew test

# Test Angular 1.4
cd $TEST_APP_ROOT/1.4
./gradlew test
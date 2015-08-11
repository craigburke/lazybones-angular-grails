#/bin/sh
source ./bin/common.sh

install_lazybones
./gradlew buildTestApps

# Test Angular 1.2
cd $TEST_APP_ROOT/1.2
./gradlew test

# Test Angular 1.3
cd $TEST_APP_ROOT/1.3
./gradlew test

# Test Angular 1.4
cd $TEST_APP_ROOT/1.4
./gradlew test
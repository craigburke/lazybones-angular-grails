#/bin/sh

java -version

# Test Angular 1.2
cd ~/clone
./gradlew buildTestApp -PangularVersion=1.2
cd test/app
./gradlew test

# Test Angular 1.3
cd ~/clone
./gradlew buildTestApp -PangularVersion=1.3
cd test/app
./gradlew test

# Build
cd ~/clone
./gradlew war
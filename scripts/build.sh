#/bin/sh

jdk_switcher use oraclejdk7

# Install lazybones
curl -s get.gvmtool.net | bash
echo "gvm_auto_answer=true" > ~/.gvm/etc/config
source ~/.gvm/bin/gvm-init.sh
gvm install lazybones
gvm use lazybones

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
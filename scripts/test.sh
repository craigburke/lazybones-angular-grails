#/bin/sh
export PROJECT_ROOT=~/clone
export APP_ROOT="$PROJECT_ROOT/test/app"

jdk_switcher use oraclejdk7

# Install lazybones
curl -s get.gvmtool.net | bash
echo "gvm_auto_answer=true" > ~/.gvm/etc/config
source ~/.gvm/bin/gvm-init.sh
gvm install lazybones
gvm use lazybones

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

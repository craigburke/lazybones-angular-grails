#!/bin/bash
set -e

LAZYBONES_VERSION="0.7.1"
SELENIUM_VERSION="2.42.2"
SELENIUM_FOLDER=${SELENIUM_VERSION%.*}

function install_java {
	apt-get -q -y install python-software-properties
	add-apt-repository -y ppa:webupd8team/java > /dev/null
	apt-get -q -y update
	echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | debconf-set-selections
	echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 seen true" | debconf-set-selections

	apt-get -q -y install oracle-java8-set-default
}

function install_lazybones {
	curl -s get.gvmtool.net | bash
	echo "gvm_auto_answer=true" > ~/.gvm/etc/config
	source ~/.gvm/bin/gvm-init.sh
	gvm install lazybones
	gvm use lazybones
}

function start_selenium {
	start xvfb
	wget "http://selenium-release.storage.googleapis.com/$SELENIUM_FOLDER/selenium-server-standalone-$SELENIUM_VERSION.jar" --quiet
	java -jar "selenium-server-standalone.$SELENIUM_VERSION.jar" > /dev/null 2>&1 &
}

install_java
install_lazybones
start_selenium

printf "Testing Angular 1.2...\n"
./gradlew buildTestApp -PangularVersion=1.2
cd test/app
./gradlew test
printf "Done testing Angular 1.2!\n"

printf "Testing Angular 1.3...\n"
cd ../../
./gradlew buildTestApp -PangularVersion=1.3
cd test/app
./gradlew test
printf "Done testing Angular 1.3!\n"

#Build
./gradlew war

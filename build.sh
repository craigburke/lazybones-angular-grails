#!/bin/bash
set -e

SELENIUM_VERSION="2.42.2"
SELENIUM_FOLDER=${SELENIUM_VERSION%.*}

function install_java {
	sudo sudo apt-get purge openjdk*
	unset JAVA_HOME
	
	sudo apt-get -q -y install python-software-properties
	sudo add-apt-repository -y ppa:webupd8team/java > /dev/null
	sudo apt-get -q -y update
	echo "oracle-java7-installer shared/accepted-oracle-license-v1-1 select true" | sudo debconf-set-selections
	echo "oracle-java7-installer shared/accepted-oracle-license-v1-1 seen true" | sudo debconf-set-selections

	sudo apt-get -q -y install oracle-java7-set-default
}

function start_selenium {
	sudo start xvfb
	wget "http://selenium-release.storage.googleapis.com/$SELENIUM_FOLDER/selenium-server-standalone-$SELENIUM_VERSION.jar" --quiet
	java -jar "selenium-server-standalone.$SELENIUM_VERSION.jar" > /dev/null 2>&1 &
}

install_java
start_selenium
./gradlew buildTestApp

#!/bin/bash

./gradlew buildTestApp
cd test/app/
./gradlew run

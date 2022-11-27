#!/bin/bash
./gradlew build
java -jar ./build/libs/test-0.0.1-SNAPSHOT.jar
pause
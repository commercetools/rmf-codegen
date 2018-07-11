#!/bin/sh
./gradlew clean build
./gradlew bintrayUpload
./gradlew :codegen-gradle-plugin:publishPlugins
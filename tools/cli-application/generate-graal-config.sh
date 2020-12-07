#!/bin/sh
mkdir -p META-INF/native-image
../../gradlew build
~/.gradle/caches/com.palantir.graal/20.2.0/11/graalvm-ce-java11-20.2.0/Contents/Home/bin/java -agentlib:native-image-agent=config-output-dir=./META-INF/native-image -jar ../../rmf-gen.jar generate -o ./build/tmp -t RAML_DOC ../../api-spec/api.raml
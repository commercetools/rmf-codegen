FROM openjdk:8-jre-alpine

WORKDIR /out

ADD raml-generic-generator/build/libs/raml-generic-generator-1.0-SNAPSHOT-all.jar /app/rmf-generator.jar

ENTRYPOINT ["java", "-jar", "/app/rmf-generator.jar"]

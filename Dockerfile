FROM openjdk:8 as builder

WORKDIR /rmf
COPY . /rmf

RUN ./gradlew clean shadowJar

FROM openjdk:8-jre-alpine

WORKDIR /app

RUN apk add --no-cache git
COPY --from=builder /rmf/tools/cli-application/build/libs/rmf-gen.jar /app/rmf-gen.jar
ADD rmf-gen.sh /app/rmf-gen.sh

ENV JAVA_OPTS ""
EXPOSE 5005
ENTRYPOINT ["./rmf-gen.sh"]

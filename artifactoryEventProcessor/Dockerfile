ARG JAVA_BASE_IMAGE=docker.io/openjdk:8-jdk-alpine
FROM ${JAVA_BASE_IMAGE}

LABEL maintainer="Lev Shulman <lshulman@redhat.com>"

RUN mkdir /app

COPY target/operator-pipeline-api-service-0.0.1-SNAPSHOT.jar /app/
 
EXPOSE 8080
WORKDIR /app/
CMD java -jar operator-pipeline-api-service-0.0.1-SNAPSHOT.jar

FROM openjdk:17

LABEL maintainer="kamieniarzk@gmail.com" version="1.0"

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

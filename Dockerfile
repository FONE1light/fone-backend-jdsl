FROM openjdk:11.0.10-jre-slim-buster
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=${USE_PROFILE}", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

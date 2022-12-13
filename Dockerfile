FROM openjdk:11.0.10-jre-slim-buster
ARG JAR_FILE=server/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=${USE_PROFILE}","-Dio.netty.tryReflectionSetAccessible=true","--add-opens","java.base/jdk.internal.misc=ALL-UNNAMED","-jar","/app.jar"]

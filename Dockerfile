FROM openjdk:17-jdk-slim
ARG JAR_FILE=server/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=${USE_PROFILE}","-Dio.netty.tryReflectionSetAccessible=true","--add-opens","java.base/jdk.internal.misc=ALL-UNNAMED","-jar","/app.jar"]

FROM openjdk:17-jdk-slim
ARG JAR_FILE=chatting/build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dio.netty.tryReflectionSetAccessible=true","--add-opens","java.base/jdk.internal.misc=ALL-UNNAMED","-jar","/app.jar"]

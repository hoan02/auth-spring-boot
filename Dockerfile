FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/demo-spring-boot-*.jar app.jar

EXPOSE 7999

ENTRYPOINT ["java", "-jar", "app.jar"]


FROM openjdk:17-jdk-slim

COPY target/arthub-0.0.1-SNAPSHOT.jar /app.jar

WORKDIR /app

CMD ["java", "-jar", "/app.jar"]

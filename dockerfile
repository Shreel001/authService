FROM eclipse-temurin:21-jdk

WORKDIR /app

RUN mkdir -p /app/config

COPY src/main/resources/application.properties /app/config/application.properties

COPY target/authService-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.additional-location=file:/app/config/application.properties"]
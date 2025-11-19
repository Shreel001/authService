FROM eclipse-temurin:21-jdk

WORKDIR /app

# Create config directory
RUN mkdir -p /app/config

# Copy the application.properties generated in GitHub Actions
COPY src/main/resources/application.properties /app/config/application.properties

# Copy the Spring Boot JAR
COPY target/authService-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.additional-location=file:/app/config/application.properties"]

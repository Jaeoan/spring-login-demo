# Build stage: use Gradle with JDK 17 to compile the Spring Boot application
FROM gradle:8.10.2-jdk17 AS builder
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle clean bootJar --no-daemon

# Runtime stage: use lightweight JRE image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

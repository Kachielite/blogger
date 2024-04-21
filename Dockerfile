# First stage: Build the application
FROM ubuntu:latest AS build

# Install Java 21
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get clean

# Set working directory
WORKDIR /app

# Copy the whole project
COPY . .

# Make the gradlew script executable
RUN chmod +x gradlew

# Build the Spring Boot application
RUN ./gradlew bootJar --no-daemon

# Create a new stage for the final image
FROM eclipse-temurin:21

# Expose port
EXPOSE 8080

# Set working directory
WORKDIR /app

# Copy built JAR file from the previous stage
COPY --from=build /app/build/libs/blogger-0.0.1-SNAPSHOT.jar /app/blogger.jar

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "/app/blogger.jar"]

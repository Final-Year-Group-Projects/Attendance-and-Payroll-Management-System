# Base image with OpenJDK 17
FROM openjdk:17-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the entire Maven project (including pom.xml and source code)
COPY . /app

# Build the Maven project
RUN mvn clean package -DskipTests

# Expose the application port
EXPOSE 8081

# Run the JAR file
ENTRYPOINT ["java", "-jar", "target/Attendance_service-0.0.1-SNAPSHOT.jar"]
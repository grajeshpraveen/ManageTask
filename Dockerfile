# Use a base image with Java and a compatible operating system (e.g., OpenJDK)
FROM openjdk:11

# Set the working directory inside the container
WORKDIR /app

# Copy your Spring Boot application JAR to the container
COPY target/manager-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port if your application needs it (change the port if necessary)
EXPOSE 8080

# Define environment variables for PostgreSQL database configuration
ENV SPRING_PROFILES_ACTIVE=dev

# Run the Spring Boot application with the specified profiles
CMD ["java", "-jar", "/app/app.jar"]

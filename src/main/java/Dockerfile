# Use Oracle Linux with JDK 21
FROM oraclelinux:9-slim

# Install JDK 21
RUN microdnf install java-21-openjdk-devel

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file from the target directory to the container
COPY target/*.jar /app/cd-server.jar

# Expose the port the application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/cd-server.jar"]

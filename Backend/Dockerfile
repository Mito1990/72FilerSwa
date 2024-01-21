# Use an official OpenJDK runtime as a parent image
FROM openjdk:17

# Set the working directory in the container
WORKDIR /Backend

# Copy the application JAR file into the container at /Backend
COPY build/libs/*.jar /Backend/72filter-0.0.1-SNAPSHOT.jar

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Specify the command to run on container startup
CMD ["java", "-jar", "72filter-0.0.1-SNAPSHOT.jar"]

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17

# Set the working directory in the container
WORKDIR /Backend

# wait-for-it.sh is used, that the spring application is waiting,
# for the DB to start and be ready, because the spring application,
# depends on the DB.
# Copy the wait-for-it.sh script into the image
COPY wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

# Copy the application JAR file into the container at /Backend
COPY build/libs/*.jar /Backend/72filter-0.0.1-SNAPSHOT.jar

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Specify the command to run on container startup
CMD ["./wait-for-it.sh", "database:3306", "--", "java", "-jar", "72filter-0.0.1-SNAPSHOT.jar"]

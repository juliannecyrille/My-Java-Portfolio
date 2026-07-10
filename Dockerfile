# Use eclipse-temurin since the legacy openjdk image has been deprecated
FROM eclipse-temurin:17-jdk

# Set the working directory inside the cloud container
WORKDIR /app

# Copy our project files into the cloud container
COPY index.html .
COPY WebInventory.java .

# Compile the backend Java logic
RUN javac WebInventory.java

# Expose the port our server uses
EXPOSE 8080

# Start command to launch the app
CMD ["java", "WebInventory"]
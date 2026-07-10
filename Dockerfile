# Use an official lightweight Java environment
FROM openjdk:17-jdk-slim

# Set the working directory inside the cloud container
WORKDIR /app

# Copy project files into the cloud container
COPY index.html .
COPY WebInventory.java .

# Compile the backend Java logic
RUN javac WebInventory.java

# Expose the port server uses
EXPOSE 8080

# Start command to launch the app
CMD ["java", "WebInventory"]
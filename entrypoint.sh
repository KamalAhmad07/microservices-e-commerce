#!/bin/bash

# Check if SERVICE_NAME environment variable is set
if [ -z "$SERVICE_NAME" ]; then
  echo "ERROR: SERVICE_NAME is not set."
  exit 1
fi

# Define the path to the JAR file based on SERVICE_NAME
JAR_FILE="${SERVICE_NAME}.jar"

# Check if the JAR file exists
if [ ! -f "$JAR_FILE" ]; then
  echo "ERROR: JAR file $JAR_FILE not found."
  exit 1
fi

# Start the service
echo "Starting service: $SERVICE_NAME"
exec java -jar "$JAR_FILE"

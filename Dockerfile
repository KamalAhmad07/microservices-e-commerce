FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app

# Copy the parent pom.xml file
COPY pom.xml ./

# Copy all module-specific pom.xml files
COPY api-gateway/pom.xml api-gateway/
COPY discovery-service/pom.xml discovery-service/
COPY inventory-service/pom.xml inventory-service/
COPY order-service/pom.xml order-service/
COPY notification-service/pom.xml notification-service/
COPY product-service/pom.xml product-service/

# Download dependencies (this will be cached if pom files don't change)
RUN mvn dependency:go-offline -B

# Copy the source code
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app

# Copy the built artifacts from the build stage
COPY --from=build /app/api-gateway/target/api-gateway-1.0-SNAPSHOT.jar api-gateway.jar
COPY --from=build /app/discovery-service/target/discovery-service-1.0-SNAPSHOT.jar discovery-service.jar
COPY --from=build /app/inventory-service/target/inventory-service-1.0-SNAPSHOT.jar inventory-service.jar
COPY --from=build /app/order-service/target/order-service-1.0-SNAPSHOT.jar order-service.jar
COPY --from=build /app/notification-service/target/notification-service-1.0-SNAPSHOT.jar notification-service.jar
COPY --from=build /app/product-service/target/product-service-1.0-SNAPSHOT.jar product-service.jar

# Copy the entrypoint script
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# Set the startup command
ENTRYPOINT ["/app/entrypoint.sh"]

FROM maven:3.9.4-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Copy Maven configuration
COPY pom.xml ./
COPY mvnw ./
COPY .mvn/ .mvn/

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build application
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy built application from builder
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

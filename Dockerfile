# Build stage - use Alpine-based Maven image (smaller)
FROM maven:3.9.5-eclipse-temurin-11-alpine AS builder
WORKDIR /app

# Copy only pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy source and build (dependencies are cached)
COPY src ./src
RUN mvn clean package -DskipTests -o

# Runtime stage - use Alpine JRE (much smaller ~150MB vs ~300MB)
FROM eclipse-temurin:11-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/task-tracker-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

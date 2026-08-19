# ==========================================
# Stage 1: Build JAR using Maven
# ==========================================
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace

# Cache dependencies first
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B

# Copy source code and build production artifact (skipping tests for build speed)
COPY src src
RUN ./mvnw clean package -DskipTests

# Extract layers to optimize Docker caching
RUN java -Djarmode=layertools -jar target/*.war extract --destination target/extracted

# ==========================================
# Stage 2: Minimal Production Runtime
# ==========================================
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

# Create a dedicated, non-root user and group
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy extracted layers from builder
COPY --from=builder /workspace/target/extracted/dependencies/ ./
COPY --from=builder /workspace/target/extracted/spring-boot-loader/ ./
COPY --from=builder /workspace/target/extracted/snapshot-dependencies/ ./
COPY --from=builder /workspace/target/extracted/application/ ./

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app
USER appuser

# JVM Memory Tuning for t3.micro:
# - UseSerialGC: Lowest memory footprint for < 1GB RAM servers
# - Xms / Xmx: Cap heap size strictly between 128MB and 256MB
ENV JAVA_OPTS="-XX:+UseSerialGC -Xms128m -Xmx256m -XX:+ExitOnOutOfMemoryError"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]

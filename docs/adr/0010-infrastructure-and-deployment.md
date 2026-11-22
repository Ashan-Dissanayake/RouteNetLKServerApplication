# ADR 0010: Infrastructure and Deployment Decisions

## Status
Accepted

## Context
We need a consistent way to build, package, and deploy the RouteNetLK Server Application across different environments (Development, Testing, and Production) while ensuring security and minimal footprint.

## Decision
We have adopted a container-first approach with multi-stage builds and environment-specific configurations.

### 1. Containerization (Docker)
- **Multi-Stage Build:** We use a two-stage Dockerfile:
    - **Builder Stage:** Uses `eclipse-temurin:17-jdk-alpine` to compile the code and extract Spring Boot layers.
    - **Runtime Stage:** Uses a minimal `eclipse-temurin:17-jre-alpine` image to run the application, reducing the attack surface and image size.
- **Layer Optimization:** We use Spring Boot's **layertools** to separate dependencies, resources, and application code into distinct layers. This significantly speeds up build times by leveraging Docker's layer caching.
- **Security:** The application runs under a **non-root user** (`appuser`) inside the container to mitigate security risks.

### 2. Environment Management
- **Profile-Based Configuration:** We use Spring Profiles (`dev`, `prod`) managed via `application-{profile}.properties`.
- **Environment Variables:** Sensitive information (DB credentials, JWT secrets, Mail server settings) is never hardcoded. We use `dotenv-java` for local development and standard environment variables for production/Docker deployments.
- **CI/CD Compatibility:** A special `ci` Maven profile exists to run tests in environments where Docker/Testcontainers might not be available, by excluding integration tests that require a live database.

### 3. Build Tooling
- **Maven Wrapper:** The `mvnw` wrapper is included in the repository to ensure consistent build results regardless of the Maven version installed on the host machine.

## Consequences

- **Pros:**
    - **Consistency:** "Build once, run anywhere" across dev, staging, and production.
    - **Security:** Minimal runtime image and non-root execution improve the security posture.
    - **Efficiency:** Faster CI/CD pipelines due to optimized Docker layering and profile-based testing.
- **Cons:**
    - **Complexity:** Requires knowledge of Docker and multi-stage build concepts.
    - **Overhead:** Managing multiple property files and environment variables requires discipline to avoid configuration drift.

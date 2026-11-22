# ADR 0001: Technologies and Frameworks

## Status
Accepted

## Context
The RouteNetLK Server Application requires the selection of appropriate technologies and frameworks. Decisions must consider efficiency, maintainability, and alignment with business requirements.

## Decision

The following technologies and frameworks have been selected for the development of the system:

### 1. Core Framework: Spring Boot 3.5.3
- **Reason:** It is a highly popular and mature framework within the Java ecosystem. Features like dependency injection, auto-configuration, and an embedded server (Tomcat) significantly increase development speed.

### 2. Language: Java 17 (LTS)
- **Reason:** Java 17 is a Long Term Support (LTS) version. It includes modern features like Records and Text Blocks, along with improved performance.

### 3. Database: MySQL 8.x
- **Reason:** A highly reliable and efficient open-source relational database for storing structured data.

### 4. Persistence Layer: Spring Data JPA (Hibernate)
- **Reason:** Used to simplify database operations and reduce boilerplate code. It provides excellent support for complex queries and auditing.

### 5. Security: Spring Security & JWT
- **Reason:** Since this is a stateless REST API, a JWT (JSON Web Token) based security system was chosen. Method-level security and custom authorization filters are utilized.

### 6. Mapping & Boilerplate Reduction: MapStruct & Lombok
- **MapStruct:** For fast and type-safe mapping between Entities and DTOs.
- **Lombok:** To automatically generate getters, setters, builders, etc., improving code readability.

### 7. Optimization & Scheduling: Timefold Solver
- **Reason:** Used for solving complex scheduling and optimization problems like roster planning and trip execution.

### 8. API Documentation: SpringDoc OpenAPI (Swagger)
- **Reason:** Helps to automatically document REST API endpoints and allows for easy testing through a UI.

### 9. Testing Tools
- **JUnit 5 & Mockito:** For unit and component testing.
- **Testcontainers (MySQL):** To use a real MySQL instance via Docker for database integration tests.

## Consequences
- **Pros:**
    - Improved system reliability due to the use of modern and stable technologies.
    - Large community support makes it easier to resolve issues.
    - Automated documentation (Swagger) benefits client-side developers.
- **Cons:**
    - The learning curve may be slightly higher due to the number of frameworks involved.
    - Testcontainers require a Docker environment to be present.

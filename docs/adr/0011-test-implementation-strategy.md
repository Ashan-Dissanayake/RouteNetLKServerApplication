# ADR 0011: Test Implementation Strategy

## Status
Accepted

## Context
The RouteNetLK Server Application requires a robust testing strategy to ensure the reliability of its modules (Branch, Vehicle, Trip, etc.). We need to define how different layers of the application are tested to maintain consistency and efficiency.

## Decision
We have implemented a multi-layered testing strategy using JUnit 5, Spring Boot Test, and Testcontainers.

### 1. Persistence Layer Testing (Repositories)
- **Tooling:** `@DataJpaTest`, `Testcontainers` (MySQL).
- **Strategy:** Integration tests are performed against a real MySQL database provided by Testcontainers to ensure JPA queries and custom repository methods work correctly.
- **Base Configuration:** A `BaseTestContainer` abstract class is used to manage the lifecycle of the MySQL container, ensuring consistency across all repository tests.

### 2. Web Layer Testing (Controllers)
- **Tooling:** `@WebMvcTest`, `MockMvc`, `Mockito`.
- **Strategy:** Isolated testing of the web layer. Services are mocked using `@MockitoBean` to focus on request mapping, validation, and security authorizations.
- **Security:** A `TestSecurityConfiguration` is used to simulate security behavior and verify method-level security (`@PreAuthorize`).

### 3. Service Layer Testing
- **Tooling:** `Mockito`.
- **Strategy:** Business logic is tested in isolation by mocking repository dependencies. (Note: Currently many modules focus on Controller and Repository tests).

### 4. Shared Configurations
- **Test Security:** Custom security configuration for tests to handle authentication and access denied scenarios specifically for the test environment.

## Consequences
- **Pros:**
    - High confidence in database-related logic due to Testcontainers.
    - Fast feedback on API contracts and security via `MockMvc`.
    - Clear separation between unit and integration tests.
- **Cons:**
    - Testcontainers require a Docker environment, which might increase the build time in CI/CD pipelines.
    - Initial overhead in setting up containers.

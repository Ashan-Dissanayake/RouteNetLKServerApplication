# ADR 0002: Overall Architecture and Component Organization

## Status
Accepted

## Context
The RouteNetLK Server Application is a complex system managing various transport-related modules like Branch, Vehicle, Trip, and Crew. We need a consistent architecture and clear guidelines on how components are organized to ensure maintainability, scalability, and developer productivity.

## Decision

We have adopted a **Layered Architecture** within a **Module-based Structure**. The application is primarily organized into modules, with shared functionality residing in a central `shared` package.

### 1. Module-based Organization
Each business domain (e.g., `branch`, `vehicle`, `user`) is encapsulated within its own package under `lk.ashan.routenetlkserverapllication.module`. This promotes high cohesion and simplifies navigation.

A typical module contains:
- `controller`: REST API endpoints.
- `service`: Business logic and orchestration.
- `repository`: Data access logic (Spring Data JPA).
- `model`:
    - `entity`: Database entities.
    - `dto`: Data Transfer Objects for API requests and responses.
- `mapper`: MapStruct or manual mappers for Entity-DTO conversion.
- `validation`: Custom validator classes for complex business rules.

### 2. Layered Architecture
Within each module and across the system, we follow a strict layering:
- **Web Layer (Controllers):** Handles HTTP requests, input validation (basic JSR-303), and calls the Service layer.
- **Service Layer:** Contains core business logic, transaction management (`@Transactional`), and security checks. It acts as a bridge between Controllers and Repositories.
- **Persistence Layer (Repositories):** Interface-based data access using Spring Data JPA.
- **Domain Layer (Entities):** Represents the data structure and basic domain constraints.

### 3. Shared Infrastructure
Common functionality is located in the `shared` package:
- `api`: Common API response structures (e.g., `APISuccessResponse`).
- `config`: Global configurations (Audit, Email, etc.).
- `exception`: Global exception handling and custom exception definitions (e.g., `ResourceNotFoundException`, `BusinessRuleViolationException`).
- `util`: Reusable utility classes (e.g., `IntervalTree`).
- `validation`: Generic validation logic.
- `transaction`: Custom annotations for controlling filters (e.g., `@DisableSoftDeleteFilter`).

### 4. Security Architecture
The system uses **Spring Security** with **JWT (JSON Web Token)** for stateless authentication.
- **Authentication:** Managed via `AuthenticationFilter` and a custom `MyAuthenticationProvider`.
- **Authorization:** Handled by `AuthorizationFilter` and method-level security (`@PreAuthorize`) in controllers/services.
- **User Details:** Custom `MyUserDetailsService` loads user information from the database.

### 5. Error Handling
A `GlobalExceptionHandler` (using `@ControllerAdvice`) catches exceptions across all modules and returns standardized error responses to the client.

## Consequences
- **Pros:**
    - **Maintainability:** Clear boundaries between modules and layers make it easier to locate and modify code.
    - **Consistency:** Developers follow a predictable pattern when adding new features.
    - **Testability:** Decoupled layers allow for effective unit and integration testing (as defined in ADR 0001).
    - **Scalability:** Module-based organization allows for easier transition to microservices if needed in the future.
- **Cons:**
    - **Boilerplate:** Some repetitive code for DTOs and Mappers.
    - **Complexity:** For very simple modules, the layering might feel like overkill.

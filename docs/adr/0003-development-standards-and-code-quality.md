# ADR 0003: Development Standards and Code Quality Decisions

## Status
Accepted

## Context
To ensure long-term maintainability, readability, and consistency across the RouteNetLK codebase, we need to establish clear development standards and quality gates.

## Decision
We have adopted a set of tools and conventions to enforce high code quality and reduce technical debt.

### 1. Reducing Boilerplate
- **Lombok:** Used extensively to generate getters, setters, constructors, and loggers, keeping domain entities and DTOs clean.
- **MapStruct:** Used for type-safe, high-performance mapping between Entities and DTOs, avoiding manual mapping code which is error-prone.

### 2. Standardized Object Modeling
- **DTO Pattern:** We strictly separate internal Entities from external API DTOs. This prevents internal database changes from breaking public APIs and allows for granular control over what data is exposed.
- **Base Classes:** Common functionality (e.g., auditing fields, IDs) is abstracted into a `BaseEntity` to ensure all entities follow the same structure.

### 3. Validation Strategy
- **Declarative Validation:** We use **Bean Validation (JSR-380)** annotations (e.g., `@NotNull`, `@Size`, `@Email`) on DTOs for simple constraints.
- **Validator Components:** For complex business rules that require database checks or multi-field logic, we use dedicated `Validator` components (e.g., `BranchValidator`) to keep services focused on orchestration.

### 4. API Documentation
- **OpenAPI/Swagger:** Every controller and DTO is documented using `SpringDoc OpenAPI` annotations, ensuring the API contract is always up-to-date with the implementation.

### 5. Testing Requirements
- **Integration Testing:** We use **Testcontainers** for repository tests to ensure that our SQL queries work correctly against a real database.
- **Unit Testing:** Services and logic-heavy components are covered with JUnit 5 and Mockito.

## Consequences

- **Pros:**
    - **Maintainability:** Standardized patterns make it easier for new developers to understand and contribute to the project.
    - **Quality:** Automated mapping and validation reduce the likelihood of common bugs.
    - **Clarity:** Clear separation between DTOs and Entities makes the data flow obvious.
- **Cons:**
    - **Learning Curve:** New developers must be familiar with Lombok, MapStruct, and the chosen validation patterns.
    - **Build Time:** Annotation processors (Lombok/MapStruct) add a slight overhead to compilation times.

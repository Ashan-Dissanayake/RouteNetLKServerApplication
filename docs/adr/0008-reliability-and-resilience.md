# ADR 0008: Reliability and Resilience Decisions

## Status
Accepted

## Context
RouteNetLK handles critical transportation data. System failures, data inconsistencies, or unexpected errors must be handled gracefully to ensure high availability and data integrity.

## Decision
We have implemented several patterns to ensure the system is resilient to failures and maintains data reliability.

### 1. Data Integrity and Consistency
- **Transactional Boundaries:** We use Spring's `@Transactional` annotation to ensure that complex business operations (e.g., creating a shift and assigning a crew) are atomic. Any failure during the process triggers a full rollback.
- **Auditing:** Every change to a record is automatically tracked using Spring Data JPA Auditing (`BaseEntity`), capturing who created/modified the record and when. This ensures accountability and helps in debugging data issues.

### 2. Standardized Error Handling
- **Global Exception Handler:** We use a centralized `GlobalExceptionHandler` to catch all runtime and checked exceptions. This prevents the application from returning raw stack traces to the user and ensures the system remains in a predictable state even after an error.
- **Domain-Specific Exceptions:** We use custom exceptions like `BusinessRuleViolationException`, `ResourceNotFoundException`, and `InvalidStateTransitionException` to provide precise feedback and allow the system to handle different error scenarios specifically.

### 3. Fail-Safe Operations
- **Soft Deletion:** Instead of hard-deleting records, we use a `deleted` flag. This protects the system from accidental data loss and maintains referential integrity for historical reporting.
- **Validation Layers:** Validation occurs at multiple layers (DTO validation, Service-level validators, and Database constraints) to ensure that only valid data enters the system.

### 4. Availability
- **Health Checks:** We leverage Spring Boot Actuator (if configured) or simple endpoint checks to allow infrastructure monitors to detect and restart unhealthy instances.
- **Statelessness:** Since the server is stateless, any instance can be replaced or restarted without losing user session data (which is stored in the JWT).

## Consequences

- **Pros:**
    - **Reliability:** Data remains consistent even in the face of unexpected application crashes.
    - **Debuggability:** Standardized error codes and auditing make it easier to identify and fix production issues.
    - **Resilience:** The system can recover quickly from individual instance failures.
- **Cons:**
    - **Overhead:** Maintaining a comprehensive auditing trail increases the database size over time.
    - **Complexity:** Developers must be careful to define correct transactional boundaries to avoid deadlocks or partial updates.

# ADR 0004: Data Management and Persistence Decisions

## Status
Accepted

## Context
The RouteNetLK Server Application handles sensitive transportation data that requires strict isolation, auditing, and recoverability. We need clear decisions on how data is stored, accessed, and managed across the system to ensure integrity and security.

## Decision

We have implemented a comprehensive data management strategy centered around Spring Data JPA and Hibernate, with several custom extensions for auditing and data isolation.

### 1. Persistence Framework: Spring Data JPA & Hibernate
- **Strategy:** All database interactions are managed through Spring Data JPA interfaces. Hibernate is used as the underlying ORM provider.
- **Rationale:** Reduces boilerplate code for CRUD operations and provides powerful abstraction for complex queries via Specifications and JPQL.

### 2. Soft Delete Strategy
- **Implementation:** Instead of physical deletion, we use a "Soft Delete" approach.
- **Mechanism:**
    - A `BaseEntity` mapped superclass contains a `deleted` boolean flag.
    - Hibernate `@FilterDef` and `@Filter` are defined on `BaseEntity` to automatically exclude records where `deleted = true`.
    - `SoftDeleteFilterAspect` (AOP) automatically enables this filter for all `get*` and `search*` methods in the service layer.
    - Custom annotation `@DisableSoftDeleteFilter` can be used to bypass this filter when necessary (e.g., in update/validation logic).

### 3. Data Isolation (Branch-based Multi-tenancy)
- **Implementation:** Data is isolated at the Branch level to ensure users only access records belonging to their respective branch.
- **Mechanism:**
    - A Hibernate filter `branchFilter` is used to inject a `branchId` parameter into queries.
    - `BranchAndUserFilterAspect` (AOP) captures the logged-in user's branch from the `SecurityContext` and applies it to the active Hibernate session.
    - Administrators (`ROLE_ADMIN`, `ROLE_SYSTEM_ADMIN`) bypass this filter automatically.
    - Custom annotation `@DisableBranchFilter` allows specific service methods to query across all branches.

### 4. Automated Auditing and Context Injection
- **Auditor Awareness:** `AuditorAwareImpl` integrates with Spring Security to provide the current `User` entity for auditing purposes.
- **Branch Context Injection:** 
    - A custom `@CurrentBranch` annotation is used on entity fields.
    - `BranchAnnotationListener` (JPA Entity Listener) uses reflection and `PrePersist` to automatically assign the logged-in user's branch to the entity if it is null.

### 5. Transaction Management
- **Strategy:** We use declarative transaction management with Spring's `@Transactional`.
- **Policy:** 
    - Service layer methods are the transaction boundaries.
    - `@Transactional(readOnly = true)` is used for all query methods to optimize performance.
    - Aspect-oriented filters (Soft Delete, Branch Isolation) are applied before these transactions/methods begin.

## Consequences

- **Pros:**
    - **Security:** Automatic data isolation prevents accidental data leakage between branches.
    - **Recoverability:** Soft delete allows for easy recovery of accidentally deleted data.
    - **Developer Productivity:** Developers don't need to manually add `WHERE branch_id = ?` or `WHERE deleted = false` to every query.
    - **Consistency:** Auditing and context injection ensure all records have accurate ownership data.
- **Cons:**
    - **Complexity:** Debugging AOP-based filters can be challenging if not properly understood.
    - **Performance Overhead:** Reflection in the `BranchAnnotationListener` and additional filters in SQL queries might introduce minor overhead.
    - **Native Queries:** Hibernate filters do not apply to native SQL queries, requiring manual handling in those rare cases.

# RouteNetLK — Backend Application

A Spring Boot backend for public transport depot operations, covering fleet management, trip scheduling, crew rostering, vehicle servicing, inventory workflows, and operational reporting.

RouteNetLK focuses on modular backend design, secure role-based access, branch-level data isolation, automated testing, and constraint-based optimization for transport operations.

**Backend:** Java 17 · Spring Boot 3.5.3 · MySQL 8

**Related repositories:** [System Overview](https://github.com/Ashan-Dissanayake/RouteNetLK) · [Angular Client](https://github.com/Ashan-Dissanayake/RouteNetLKClientApplication)

---

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Architecture](#architecture)
- [Domain Modules](#domain-modules)
- [Constraint-Based Optimization](#constraint-based-optimization)
- [Security and Data Isolation](#security-and-data-isolation)
- [API Design and Error Handling](#api-design-and-error-handling)
- [Persistence and Event-Driven Workflows](#persistence-and-event-driven-workflows)
- [Testing Strategy](#testing-strategy)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Docker and CI/CD](#docker-and-cicd)
- [Engineering Decisions](#engineering-decisions)
- [Limitations and Future Improvements](#limitations-and-future-improvements)
- [Related Repositories](#related-repositories)
- [Author](#author)

## Overview

Public transport depot operations involve interconnected workflows such as vehicle allocation, crew scheduling, trip execution, maintenance, inventory management, and incident handling.

RouteNetLK provides a backend foundation for managing these workflows through a modular application architecture. It combines conventional business processing with constraint-based optimization, role-based authorization, and branch-scoped data access.

The application is designed to demonstrate practical backend engineering concepts, including domain separation, transaction management, validation, persistence, testing, and deployment automation.

## Key Features

- **Fleet Management:** Manage vehicles, servicing records, incidents, and vehicle allocations.
- **Trip Management:** Support trip scheduling and execution workflows.
- **Crew Rostering:** Model crew assignments while considering scheduling constraints.
- **Constraint-Based Optimization:** Use Timefold Solver to represent and evaluate operational scheduling constraints.
- **Inventory Management:** Support spare-part requisitions, goods receiving, and related workflows.
- **Secure Authentication:** Stateless JWT-based authentication with role- and privilege-based authorization.
- **Branch-Level Data Isolation:** Scope data access according to the user's branch and permissions.
- **Consistent API Responses:** Standardized success and error response structures.
- **Event-Driven Business Workflows:** Use Spring application events to coordinate selected operational updates.
- **Automated Testing:** Combine unit, web-layer, database integration, and optimization constraint tests.
- **Containerization and CI/CD:** Include Docker support and a GitHub Actions workflow.

## Architecture

RouteNetLK uses a modular, layered backend architecture. Domain modules organize related business capabilities, while layers separate request handling, business logic, persistence, and data representation.

<p align="center">
  <img width="300" alt="Screenshot 2026-09-30 132954" src="https://github.com/user-attachments/assets/392fb010-301d-487d-8543-23680af113d7" />

</p>

The application is organized as a modular backend rather than a collection of independently deployed microservices.


### Layer Responsibilities

| Layer | Responsibility |
|---|---|
| Controller | Handle HTTP requests, request parameters, and API responses |
| Service | Implement business rules, workflow coordination, and transaction boundaries |
| Repository | Provide persistence operations using Spring Data JPA and Hibernate |
| Entity | Represent persisted domain data |
| DTO | Define request and response data structures |
| Mapper | Convert between entities and DTOs using MapStruct |
| Security | Authenticate requests and enforce authorization rules |
| Shared Infrastructure | Provide reusable functionality such as validation, exceptions, auditing, and common utilities |

### Module Structure

Domain modules generally follow a consistent structure:

```text
module/
├── controller/
├── mapper/
├── model/
│   ├── dto/
│   └── entity/
├── repository/
└── service/
```

The exact structure may vary according to each module's requirements.

This organization helps keep business logic separated from HTTP handling and persistence concerns while allowing related functionality to remain grouped by domain.

## Domain Modules

The backend includes modules covering the following operational areas.

| Module | Responsibility |
|---|---|
| Branch | Depot and branch management |
| Crew | Crew-related operations |
| Employee | Employee information and management |
| Fare Collection | Fare collection and reconciliation workflows |
| GRN | Goods receiving |
| Incident | Incident management |
| Incident Vehicle Allocation | Vehicle allocation for incidents |
| Part Request | Spare-part requisition workflows |
| Permit | Permit processing and management |
| Privilege | Permission management |
| Roster | Crew rostering |
| Spare Part | Spare-part management |
| Trip | Trip management and scheduling |
| Trip Execution | Operational trip execution |
| User | User account management |
| Vehicle | Fleet and vehicle management |
| Vehicle Service | Vehicle servicing records |

Additional components cover dashboards, reports, security, and shared infrastructure.

### Business Workflows

The application supports workflows including:

- Vehicle lifecycle and servicing management.
- Route permit processing.
- Trip scheduling and execution.
- Crew assignment and rostering.
- Incident handling and emergency vehicle allocation.
- Spare-part requisition and issuance.
- Goods receiving.
- Fare reconciliation.

Business rules and state transitions are handled through the service layer rather than being left entirely to controllers.

## Constraint-Based Optimization

RouteNetLK integrates **Timefold Solver** to model scheduling and resource-allocation problems involving operational constraints.

Scheduling problems are not simply CRUD operations: a proposed assignment must satisfy compatibility requirements while balancing competing operational needs.

### Constraint Areas

The optimization logic considers requirements such as:

- Designation compatibility for crew assignments.
- Prevention of overlapping shifts.
- Driver and conductor requirements.
- Workload distribution across crew members.

### Why Constraint-Based Optimization?

A conventional implementation based on fixed assignment rules can become difficult to maintain when multiple constraints interact. A constraint solver provides a structured way to evaluate candidate solutions against defined rules.

The intended optimization workflow is to represent the scheduling problem, evaluate candidate assignments against the constraints, and select a solution according to the implemented scoring rules.

The actual planning entities, planning variables, hard and soft constraints, and score calculation should be documented alongside the corresponding solver implementation. No specific optimization improvement or performance figure is claimed here without benchmark evidence.

## Security and Data Isolation

Security is implemented using Spring Security and JWT-based authentication.

### Authentication

- Stateless JWT authentication.
- Password hashing using BCrypt.
- Custom authentication provider.
- JWT request filter.
- Custom authenticated principal.
- Failed-login tracking and temporary account/IP lockout using a cache-based mechanism.
- CORS and HTTP security-header configuration.

### Authorization

The application supports role- and privilege-based authorization.

Example roles:

- `ROLE_DEPOT_MANAGER`
- `ROLE_INVENTORY_OFFICER`
- `ROLE_SYSTEM_ADMIN`

Example privileges:

- `READ_VEHICLE`
- `CREATE_TRIP`

Authorization rules are used to control access to protected operations according to the authenticated user's permissions.

### Branch-Level Data Isolation

The application uses Hibernate filtering to support branch-scoped data access. Users with the required system-level privileges may access data beyond a single branch where the authorization rules permit it.

Branch isolation must be enforced consistently across relevant data-access paths. Hibernate filters should not be treated as the sole security boundary; filter activation, query behavior, privileged access, and cross-branch access rules must be verified through testing.

The application also supports soft deletion where applicable.

## API Design and Error Handling

The backend uses standardized success and error response structures to make API behavior more consistent for client applications.

### Success Response

```json
{
  "status": 200,
  "message": "Request completed successfully",
  "data": {},
  "timestamp": "2026-01-01T10:00:00"
}
```

The example illustrates the response structure; the actual timestamp format and field values depend on the implementation.

### Error Response

```json
{
  "status": 404,
  "errorCode": "RESOURCE_NOT_FOUND",
  "message": "The requested resource was not found",
  "timestamp": "2026-01-01T10:00:00"
}
```

### Exception Mapping

The global exception-handling layer maps application and framework exceptions to consistent API error responses.

| Exception | HTTP Status | Error Code |
|---|---:|---|
| `ResourceNotFoundException` | 404 | `RESOURCE_NOT_FOUND` |
| `ResourceExistsException` | 409 | `RESOURCE_EXISTS` |
| `BusinessRuleViolationException` | 422 | `RULE_VIOLATION` |
| `InvalidStateTransitionException` | 400 | `INVALID_STATE_TRANSITION` |
| `MethodArgumentNotValidException` | 400 | `VALIDATION_FAILED` |
| `BadCredentialsException` | 401 | `AUTHENTICATION_FAILED` |
| `LockedException` | 423 | `ACCOUNT_LOCKED` |

Validation errors and business-rule failures are handled centrally to reduce duplicated error-handling logic across controllers.

## Persistence and Event-Driven Workflows

### Persistence

The application uses Spring Data JPA, Hibernate, and MySQL for relational persistence.

Persistence-related capabilities include:

- Repository-based data access.
- Transaction management for business operations.
- Custom queries where required by domain workflows.
- Branch-scoped data access.
- Soft deletion.
- JPA auditing.
- Database-backed sequence records for generating reference numbers.

Examples of business reference formats include:

```text
PRM-CLM-0001
TRP-2026-0042
```

These examples illustrate the intended format; actual generation rules depend on the relevant sequence configuration and implementation.

### Spring Application Events

Selected workflows publish application events to coordinate related business operations.

Examples include:

- `PermitTransferredEvent`
- `FareReconciledEvent`
- `PartRequestApprovedEvent`
- `PartReceivedEvent`

These events can help separate the initiating business operation from related processing, such as notifications and operational updates.

Spring application events are in-process events. They should not be assumed to provide durable delivery or automatic asynchronous execution unless additional mechanisms are configured.

### Shared Infrastructure

Reusable infrastructure includes:

- JavaMailSender and Thymeleaf for email generation.
- MapStruct for entity-to-DTO mapping.
- Shared validation utilities for Sri Lankan telephone numbers, NICs, and vehicle registration numbers.
- Database-backed reference-number generation.
- Common exception-handling and validation components.

## Testing Strategy

The testing strategy combines isolated unit tests with web-layer, persistence, security, and optimization tests.

| Testing Area | Tools | Purpose |
|---|---|---|
| Unit Testing | JUnit 5, Mockito | Verify service logic and business rules |
| Web Layer | `@WebMvcTest`, MockMvc | Test HTTP responses, request validation, and controller behavior |
| Security | Spring Security Test | Verify authentication and authorization behavior |
| Database Integration | Testcontainers, MySQL | Validate persistence behavior and database-dependent queries |
| Optimization | Timefold `ConstraintVerifier` | Test scheduling constraints independently |

### Important Verification Areas

For a backend with this feature set, the most valuable tests include:

- Invalid requests return the expected validation response.
- Authentication failures and unauthorized requests are handled correctly.
- Users cannot access records outside their authorized branch scope.
- Service-level state transitions reject invalid operations.
- Database-dependent queries behave correctly against MySQL.
- Timefold constraints produce the expected outcomes for representative scheduling scenarios.
- Transactions preserve data consistency when a business operation fails.

The tools listed above describe the testing approach. Actual test counts, coverage percentages, and CI results should be added only when measured from the project.

### Run Tests

```bash
./mvnw test
```

On Windows PowerShell:

```powershell
.\mvnw.cmd test
```

If the Maven Wrapper is not included in the repository, use the installed Maven command instead:

```bash
mvn test
```

Testcontainers-based tests may require Docker to be installed and running.

## Technology Stack

| Category | Technologies |
|---|---|
| Language | Java 17 |
| Backend Framework | Spring Boot 3.5.3 |
| Security | Spring Security 6, JWT, BCrypt |
| Database | MySQL 8 |
| ORM and Persistence | Spring Data JPA, Hibernate |
| Optimization | Timefold Solver 1.32.0 |
| Object Mapping | MapStruct |
| Utilities | Lombok, Guava |
| Email and Templates | Spring Boot Mail, Thymeleaf |
| Unit Testing | JUnit 5, Mockito |
| Web Testing | MockMvc, Spring Security Test |
| Integration Testing | Testcontainers |
| Containerization | Docker |
| CI/CD | GitHub Actions |

## Getting Started

### Prerequisites

Install the following:

- JDK 17 or later compatible with the project.
- MySQL 8.
- Docker, if running Testcontainers-based tests.
- Maven, if the repository does not include the Maven Wrapper.

### 1. Clone the Repository

```bash
git clone https://github.com/Ashan-Dissanayake/RouteNetLK.git
cd RouteNetLK
```

> If this repository is the overall system repository rather than the backend repository, navigate to the backend application directory before running the commands below.

### 2. Create the Database

Create a MySQL database:

```sql
CREATE DATABASE routenetlk
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

### 3. Configure the Application

Configure the database connection and other required settings through the application's configuration files and environment variables.

Example environment variable names:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

These names are illustrative. Use the exact property names referenced by the current application configuration.

Do not commit production credentials, JWT secrets, or other sensitive configuration values to the repository.

### 4. Build the Application

Using the Maven Wrapper on Linux or macOS:

```bash
./mvnw clean package
```

Using the Maven Wrapper on Windows:

```powershell
.\mvnw.cmd clean package
```

Or, if Maven is installed:

```bash
mvn clean package
```

### 5. Run the Application

Linux or macOS:

```bash
./mvnw spring-boot:run
```

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

The default Spring Boot port is `8080`, unless changed in the application configuration.

## Docker and CI/CD

The repository includes a Dockerfile and a GitHub Actions workflow at:

```text
.github/workflows/deploy.yml
```

Docker provides a containerized packaging option, while GitHub Actions can automate the steps defined in the workflow.

The exact build, test, image-publishing, deployment, and environment-configuration steps should be described according to the actual workflow implementation. Deployment provider, public URL, required secrets, and pipeline status should be documented only after they have been verified.

## Engineering Decisions

The following decisions reflect important backend engineering concerns addressed by the project.

### Modular Layered Architecture

Organizing functionality into domain modules and separating controllers, services, repositories, and data models helps keep responsibilities distinct as business workflows grow.

### Relational Persistence

MySQL and JPA provide relational data management for interconnected operational entities. Transaction boundaries are important when workflows modify multiple related records.

### Constraint-Based Scheduling

Timefold provides a dedicated model for evaluating candidate assignments against scheduling constraints, instead of embedding every combination of rules in ad hoc assignment logic.

### Role- and Privilege-Based Authorization

Separating roles from privileges allows access rules to be expressed at different levels of granularity. Authentication identifies the user; authorization determines which operations the user can perform.

### Branch-Scoped Data Access

Branch-level filtering supports operational separation between depots. It requires careful testing because incorrect filter activation or privileged query paths could expose records beyond the intended scope.

### Integration Testing Against MySQL

Testcontainers makes it possible to test database-dependent behavior against a real MySQL instance rather than relying exclusively on mocks or an in-memory database.

These decisions involve trade-offs. Their effectiveness should be evaluated against actual requirements, implementation complexity, operational constraints, and test evidence rather than treating any technology as universally optimal.

## Limitations and Future Improvements

Potential improvements include:

- Publish interactive API documentation using OpenAPI/Swagger.
- Document Timefold planning entities, variables, score rules, and optimization results.
- Add reproducible scheduling benchmarks using representative datasets.
- Expand negative security tests for branch isolation and privileged access.
- Document the deployment environment and CI/CD pipeline with verified evidence.
- Add architecture diagrams, screenshots, and a short end-to-end demonstration.
- Evaluate durable event delivery if operational requirements demand guaranteed processing across application restarts.

These items are improvement opportunities, not claims that the corresponding functionality is already implemented.

## Related Repositories

- **System Overview:** [RouteNetLK](https://github.com/Ashan-Dissanayake/RouteNetLK)
- **Angular Client Application:** [RouteNetLK Client Application](https://github.com/Ashan-Dissanayake/RouteNetLKClientApplication)

## Author

**Ashan Dissanayake** — Full-Stack Software Engineer

- LinkedIn: [Ashan Dissanayake](https://www.linkedin.com/in/Ashan-PDissanayake)
- GitHub: [Ashan-Dissanayake](https://github.com/Ashan-Dissanayake)
- Email: [ashanpathum899@gmail.com](mailto:ashanpathum899@gmail.com)

---

*RouteNetLK is a software engineering project demonstrating backend application design, transport operations workflows, security, persistence, testing, and constraint-based optimization.*
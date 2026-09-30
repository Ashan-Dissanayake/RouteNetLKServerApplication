# RouteNetLK Server Application

**Spring Boot backend for public transport depot operations, scheduling, fleet management, business processing, and resource optimization.**

The **RouteNetLK Server Application** provides the backend services for the RouteNetLK platform. It is responsible for REST APIs, business logic, persistence, authentication and authorization, operational workflows, real-time communication, and constraint-based optimization.

The application is built with **Java 17** and **Spring Boot 3** and uses **MySQL** for persistent data storage.

---

## Architecture

The backend follows a **modular layered architecture**, organizing functionality by business domain while keeping shared infrastructure and security concerns centralized.

<p align="center">
  <img src="https://github.com/user-attachments/assets/52195e34-d9a1-4474-bd34-cce742433c4b" width="300" height="750" alt="Backend Architecture">
</p>

### Layer Responsibilities

| Layer                     | Responsibility                                                                                       |
| ------------------------- | ---------------------------------------------------------------------------------------------------- |
| **Security**              | Authentication, JWT validation, authorization, account protection, and security filters              |
| **Controller**            | REST endpoints, request validation, and HTTP response handling                                       |
| **Service**               | Business rules, transactional operations, workflow processing, and coordination between domains      |
| **Repository**            | Database access through Spring Data JPA and custom queries                                           |
| **Entity / DTO**          | Persistence models and API data transfer models                                                      |
| **Shared Infrastructure** | Exception handling, auditing, notifications, filtering, email, and other cross-cutting functionality |

Business workflows are implemented through explicit service-level business rules and controlled status transitions appropriate to each domain.

---

## Domain Modules

The backend is organized into independent domain modules under:

```text
lk.ashan.routenetlkserverapllication.module
```

```text
module/
├── branch                    # Depot and branch management
├── crew                      # Driver and conductor management
├── employee                  # Employee records and designations
├── farecollection            # Fare collection and reconciliation
├── grn                       # Goods Received Notes and stock updates
├── incident                  # Operational and vehicle incidents
├── incidentvehicleallocation # Emergency vehicle allocation
├── partreqest                # Spare part requisitions
├── permit                    # Route permits and classifications
├── privilege                 # Roles and privileges
├── roster                    # Crew roster management and optimization
├── sparepart                 # Spare part catalogue and inventory
├── trip                      # Timetable and trip scheduling
├── tripexecution             # Daily trip execution and dispatch
├── user                      # User accounts and branch assignments
├── vehicle                   # Fleet management
└── vehicleservice            # Vehicle maintenance and service history
```

Additional backend components include:

```text
dashboard/    # Operational dashboard metrics
report/       # Reporting and analytical queries
security/     # Authentication and authorization
shared/       # Common infrastructure and cross-cutting concerns
```

### Module Structure

Domain modules generally follow a consistent structure:

```text
module/<domain>/
├── controller/
├── mapper/
├── model/
│   ├── dto/
│   └── entity/
├── repository/
└── service/
```

Individual modules may contain additional packages where required by their implementation.

---

## Business Logic & Workflows

Business rules are handled within the domain services and supporting domain components.

The backend manages workflows such as:

* Vehicle lifecycle operations
* Route permit processing
* Trip scheduling and execution
* Crew assignment and rostering
* Incident processing
* Emergency vehicle allocation
* Spare part requisition and issuance
* Vehicle servicing
* Goods receiving
* Fare reconciliation

Operations that require controlled status changes validate the requested transition against the current domain state before modifying the persisted record.

This keeps business rules close to the operations they govern without introducing unnecessary architectural abstractions.

---

## Constraint-Based Optimization

RouteNetLK uses **Timefold Solver** for operational scheduling and resource allocation problems.

### Crew Rostering

The roster module uses constraint-based optimization to assign employees to shifts while considering operational requirements.

Examples of constraints include:

* Employee designation compatibility
* Shift overlap prevention
* Driver/conductor assignment requirements
* Workload distribution

### Trip Execution & Dispatch

The trip execution domain also uses optimization to support vehicle and crew allocation according to operational constraints.

Timefold allows these scheduling problems to be expressed as constraints rather than relying entirely on manually constructed assignment logic.

---

## Security

The backend uses **Spring Security** with stateless JWT-based authentication.

### Authentication

* JWT-based authentication
* Stateless session management
* BCrypt password hashing
* Custom authentication provider
* Failed login tracking
* Temporary account/IP lockout

### Authorization

The application supports both role-level and privilege-level authorization.

Examples include:

```text
ROLE_DEPOT_MANAGER
ROLE_INVENTORY_OFFICER
ROLE_SYSTEM_ADMIN
```

and fine-grained privileges such as:

```text
READ_VEHICLE
CREATE_TRIP
```

### Login Protection

Repeated authentication failures are tracked using a thread-safe cache-based mechanism. Accounts or IP addresses exceeding the configured failed-attempt threshold are temporarily locked.

### Security Configuration

The backend also provides:

* CORS configuration
* Security headers
* JWT request filtering
* Method/request authorization
* Custom authenticated user principals

---

## API

The backend exposes RESTful APIs consumed by the RouteNetLK client application.

Successful and failed operations use standardized response structures.

### Success Response

```json
{
  "status": "SUCCESS",
  "message": "Operation completed successfully",
  "data": {},
  "timestamp": "2026-08-21T11:27:36"
}
```

### Error Response

```json
{
  "status": "ERROR",
  "errorCode": "RULE_VIOLATION",
  "message": "Invalid combination: Type C buses cannot be used on Inter Provincial route.",
  "timestamp": "2026-08-21T11:27:36"
}
```

### Exception Handling

Application exceptions are centrally handled through `GlobalExceptionHandler`.

| Exception                         | HTTP Status | Error Code                 |
| --------------------------------- | ----------: | -------------------------- |
| `ResourceNotFoundException`       |       `404` | `RESOURCE_NOT_FOUND`       |
| `ResourceExistsException`         |       `409` | `RESOURCE_EXISTS`          |
| `BusinessRuleViolationException`  |       `422` | `RULE_VIOLATION`           |
| `InvalidStateTransitionException` |       `400` | `INVALID_STATE_TRANSITION` |
| `MethodArgumentNotValidException` |       `400` | `VALIDATION_FAILED`        |
| `BadCredentialsException`         |       `401` | `AUTHENTICATION_FAILED`    |
| `LockedException`                 |       `423` | `ACCOUNT_LOCKED`           |

---

## Persistence & Data Isolation

The backend uses:

* Spring Data JPA
* Hibernate ORM
* MySQL 8

<p align="center">
  <img src="https://github.com/user-attachments/assets/2cdf1d40-61e0-4281-8cc6-bca6da3658d1" width="500" height="235" alt="Persistence and Data Isolation">
</p>

### Branch-Level Data Isolation

Depot and branch users operate within their assigned branch scope.

The backend applies Hibernate filtering to automatically restrict queries according to the authenticated user's branch.

System-level users can access data outside an individual branch where their privileges permit it.

### Soft Deletion

Common domain entities support soft deletion rather than immediate physical removal.

Deleted records are excluded from normal application queries while dedicated operations can access historical records when required.

---

## Event-Driven Features

Spring Application Events are used where a domain operation needs to trigger secondary actions without tightly coupling those actions to the original service operation.

Examples include:

* `PermitTransferredEvent`
* `FareReconciledEvent`
* `PartRequestApprovedEvent`
* `PartReceivedEvent`

These events are used for actions such as notifications and related operational updates.

---

## Cross-Cutting Infrastructure

Common backend functionality is maintained under:

```text
lk.ashan.routenetlkserverapllication.shared
```

### Reference Number Generation

Database-backed sequence records generate standardized business reference numbers for domain documents.

Examples:

```text
PRM-CLM-0001
TRP-2026-0042
```

### Email Notifications

Transactional emails are generated using:

* `JavaMailSender`
* Thymeleaf templates

Email templates are maintained under:

```text
src/main/resources/templates/email/
```

### Auditing

JPA auditing records relevant entity modification metadata through the application's auditing configuration.

### Shared Validation Utilities

Common validation utilities provide reusable validation rules for application-specific data such as:

* Sri Lankan telephone numbers
* NIC formats
* Vehicle registration numbers

---

## Testing

The backend contains multiple levels of automated testing.

### Unit Testing

* JUnit 5
* Mockito

Used for testing domain services and isolated business logic.

### Web Layer Testing

Spring MVC tests use:

* `@WebMvcTest`
* `MockMvc`
* Spring Security Test

These verify REST endpoints, validation, authorization, and response handling.

### Database Integration Testing

**Testcontainers** is used to run integration tests against a real MySQL container.

This allows repository tests to verify:

* Custom queries
* Native SQL
* Persistence behavior
* Database-specific functionality
* Hibernate filtering

### Optimization Testing

Timefold `ConstraintVerifier` is used to verify optimization constraints independently.

---

## Technology Stack

| Category             | Technologies                              |
| -------------------- | ----------------------------------------- |
| **Language**         | Java 17                                   |
| **Framework**        | Spring Boot 3.5.3, Spring MVC             |
| **Persistence**      | Spring Data JPA, Hibernate ORM            |
| **Database**         | MySQL 8                                   |
| **Optimization**     | Timefold Solver 1.32.0                    |
| **Security**         | Spring Security 6, JJWT                   |
| **Mapping**          | MapStruct                                 |
| **Utilities**        | Lombok, Google Guava                      |
| **Email**            | Spring Boot Mail, Thymeleaf               |
| **Testing**          | JUnit 5, Mockito, MockMvc, Testcontainers |
| **Containerization** | Docker                                    |
| **CI/CD**            | GitHub Actions                            |

---

## Project Structure

```text
RouteNetLKServerApplication/
├── .github/
│   └── workflows/
│       └── deploy.yml
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── lk/ashan/routenetlkserverapllication/
│   │   │       ├── RouteNetLKServerApplication.java
│   │   │       ├── dashboard/
│   │   │       ├── module/
│   │   │       ├── report/
│   │   │       ├── security/
│   │   │       └── shared/
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           └── email/
│   │
│   └── test/
│       └── java/
│           └── lk/ashan/routenetlkserverapllication/
│
├── Dockerfile
├── pom.xml
└── README.md
```

---

## Local Development

### Prerequisites

* JDK 17+
* Maven 3.8+ or Maven Wrapper
* MySQL 8+
* Docker — required for Testcontainers integration tests

### Clone

```bash
git clone https://github.com/Ashan-Dissanayake/RouteNetLKServerApplication.git
cd RouteNetLKServerApplication
```

### Database

Create the development database:

```sql
CREATE DATABASE routenetlk
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

Configure the required database, JWT, and mail properties in:

```text
src/main/resources/application.properties
```

Sensitive values should be supplied through environment variables rather than committed to source control.

### Build

Linux / macOS:

```bash
./mvnw clean package
```

Windows:

```bash
mvnw.cmd clean package
```

### Run Tests

```bash
./mvnw test
```

Windows:

```bash
mvnw.cmd test
```

### Start the Application

```bash
./mvnw spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

---

## Related Repositories

* **[RouteNetLK System Overview](https://github.com/Ashan-Dissanayake/RouteNetLK)** — Overall system documentation and infrastructure.
* **[RouteNetLK Client Application](https://github.com/Ashan-Dissanayake/RouteNetLKClientApplication)** — Angular frontend application.

---

## Engineering Highlights

* **Modular Backend Architecture** — Domain functionality is organized into focused business modules.
* **Constraint-Based Scheduling** — Timefold Solver handles complex roster and dispatch optimization.
* **Stateless Security** — JWT authentication with role and privilege-based authorization.
* **Branch-Level Data Isolation** — Hibernate filtering limits branch users to their permitted operational scope.
* **Real Database Integration Testing** — Testcontainers validates persistence behavior against MySQL.
* **Event-Based Decoupling** — Spring Application Events separate selected secondary operations from core business transactions.
* **Containerized Runtime** — Docker provides a consistent backend runtime for development and deployment.

---

## Author

**Ashan Dissanayake**
*Full-Stack Software Engineer*

* **LinkedIn:** https://www.linkedin.com/in/Ashan-PDissanayake
* **GitHub:** https://github.com/Ashan-Dissanayake
* **Email:** [ashanpathum899@gmail.com](mailto:ashanpathum899@gmail.com)

# RouteNetLK Server Application

**A Spring Boot backend engineered for depot-level fleet management, operational scheduling, workflow processing, and constraint-based resource optimization.**

The **RouteNetLK Server Application** serves as the core business logic, persistence, security, and optimization engine of the RouteNetLK public transport management platform. Built on **Java 17** and **Spring Boot 3**, it exposes secure RESTfull APIs that govern end-to-end depot operations including vehicle lifecycle tracking, crew management, route permits, timetable scheduling, incident handling, spare part inventory, Goods Received Notes (GRN), and fare reconciliation.

Beyond standard CRUD operations, this backend addresses complex enterprise engineering challenges: **NP-hard crew and dispatch scheduling using constraint satisfaction (Timefold Solver)**, **decoupled business rule enforcement using the Strategy Pattern**, **deterministic lifecycle control using the State Pattern**, **aspect-oriented multi-tenant branch data scoping and soft-deletion using Hibernate filters**, and **stateless JWT security with brute-force lockout protection**.

---

> [!NOTE]
> This repository contains exclusively the **backend server application**. The client application and cloud infrastructure are maintained in separate repositories:
> - **System Overview & Architecture:** [RouteNetLK System Overview](https://github.com/Ashan-Dissanayake/RouteNetLK)
> - **Frontend Web Client:** [RouteNetLK Client Application](https://github.com/Ashan-Dissanayake/RouteNetLKClientApplication)

---

## Architecture

The backend implements a **Modular Layered Architecture** with strict package-by-feature domain boundaries. Domain modules operate independently while relying on centralized cross-cutting infrastructure in the `shared` and `security` packages.

<p align="center">
  <img src="https://github.com/user-attachments/assets/52195e34-d9a1-4474-bd34-cce742433c4b" width="300" height="750" alt="Modular Layered Architecture">
</p>

### Layer Responsibilities

| Layer | Primary Responsibilities |
|---|---|
| **Security Layer** | Intercepts HTTP requests, validates JWT claims, checks rate-limits and account lockouts, and populates the `SecurityContextHolder` with `CustomUserPrincipal` authorities. |
| **Controller Layer** | Maps HTTP requests to domain operations, enforces Jakarta input validation (`@Valid`), delegates to services, and packages results into standardized `APISuccessResponse<T>` envelopes. |
| **Service Layer** | Orchestrates business workflows, demarcates transactional boundaries (`@Transactional`), coordinates validation strategies, triggers state transition hooks, and dispatches domain events. |
| **Domain Layer** | Encapsulates business logic through validation strategy beans, concrete state transition handlers, and Timefold planning models. |
| **Persistence Layer** | Manages relational entities, custom JPQL/native queries, and projection interfaces. Transparently applies Hibernate filters for branch isolation and soft-delete suppression. |
| **Shared Infrastructure** | Centralizes cross-cutting concerns: Global exception handling (`@RestControllerAdvice`), sequential reference number generation, email delivery, auditing, and logging. |

---

## Domain & Module Organization

Functionality is organized into dedicated, self-contained domain modules located under `lk.ashan.routenetlkserverapllication.module`.

```
lk.ashan.routenetlkserverapllication
├── dashboard                     # Executive KPIs, shift coverage, and active incident metrics
├── module
│   ├── branch                    # Depots, regional offices, and branch configurations
│   ├── crew                      # Driver and conductor profiles, licenses, and medical clearances
│   ├── employee                  # Staff records, designations, and demographic validation
│   ├── farecollection            # Daily revenue logs, cash/digital collection, and reconciliation
│   ├── grn                       # Goods Received Notes, supplier receipt processing, and stock updates
│   ├── incident                  # Road breakdowns, mechanical issues, and operational disruptions
│   ├── incidentvehicleallocation # Emergency replacement bus assignment with capacity controls
│   ├── partreqest                # Depot spare part requisitions, approval lifecycle, and issuance
│   ├── permit                    # Route permits, route classifications, and transfer tracking
│   ├── privilege                 # Fine-grained authorization authorities and role-privilege mappings
│   ├── roster                    # Crew shift scheduling and Timefold roster optimization
│   ├── sparepart                 # Parts catalogue, reorder thresholds, and unit prices
│   ├── trip                      # Timetable definitions, route frequencies, and schedule templates
│   ├── tripexecution             # Live daily trip dispatches, check-ins, and Timefold vehicle/crew dispatch
│   ├── user                      # User accounts, authentication credentials, and branch assignments
│   ├── vehicle                   # Bus fleet records, chassis/engine numbers, and seating capacities
│   └── vehicleservice            # Preventive maintenance schedules, garage logs, and service history
├── report                        # Analytical projection queries and cross-domain operational reports
├── security                      # JWT utilities, authentication providers, and filter configurations
└── shared                        # Cross-cutting filters, exceptions, auditing, mail, and notifications
```

### Module Structure Pattern

Each domain module adheres to a consistent internal package structure:

```
module/<domain>/
├── controller/                   # HTTP REST Controllers
├── mapper/                       # MapStruct compile-time DTO-Entity mappers
├── model/
│   ├── dto/                      # Request, response, and summary transfer objects
│   └── entity/                   # JPA persistence entities extending BaseEntity
├── repository/                   # Spring Data JPA repositories with custom query methods
├── service/                      # Transactional business service implementations
├── state/                        # State pattern interfaces, status factories, and transition handlers
└── validation/                   # Strategy pattern validation interfaces and concrete rules
```

---

## Validation Architecture

To prevent large, deeply nested conditional statements (`if-else` blocks) inside service classes, validation logic is structured using the **Strategy Pattern** paired with a **Validation Context**.

<p align="center">
  <img src="https://github.com/user-attachments/assets/067bf7ac-29c1-459f-89f8-3cad3f0ca073" width="600" height="413" alt="Validation Architecture">
</p>

### Key Engineering Benefits:
1. **Open/Closed Principle (OCP):** New domain rules can be added by implementing a new strategy class annotated with `@Component` without modifying the core service.
2. **Single Responsibility Principle (SRP):** Each strategy encapsulates a single invariant and its associated repository lookups.
3. **Automated Spring Injection:** Services inject `List<TValidationStrategy>`, automatically receiving and running all active strategy beans in the pipeline.

---

## Workflow & State Management

Entities governed by complex operational lifecycles implement the **State Pattern** combined with a **Transition Handler** to guarantee that transitions occur deterministically and execute necessary entry/exit side effects.

### State Transition Lifecycle: Incident Management Example

<p align="center">
  <img src="https://github.com/user-attachments/assets/7883e211-263a-4313-8389-2480bd3c9831" width="400" height="523" alt="State Transition Diagram">
</p>

### Implementation Mechanics:
- **State Interface:** (e.g., [`IncidentState`](src/main/java/lk/ashan/routenetlkserverapllication/module/incident/state/IncidentState.java), [`TripState`](src/main/java/lk/ashan/routenetlkserverapllication/module/trip/state/TripState.java), [`PartRequestState`](src/main/java/lk/ashan/routenetlkserverapllication/module/partreqest/state/PartRequestState.java)) declares `transitionTo(entity, newStatus)` and `validateInitial()`.
- **Concrete State Classes:** (e.g., `IncidentReportedState`, `IncidentInProgressState`) maintain lists of permitted target states and throw [`InvalidStateTransitionException`](src/main/java/lk/ashan/routenetlkserverapllication/shared/exception/InvalidStateTransitionException.java) on illegal transitions.
- **Status Factory:** (e.g., `IncidentStatusFactory`) dynamically resolves the corresponding Spring bean for any given status entity or string.
- **Transition Handler:** (e.g., `IncidentStateTransitionHandler`, `TripStateTransitionHandler`) wraps transitions with lifecycle hooks (`executeOnExit` and `executeOnEnter`), auditing transitions and triggering side effects (e.g., releasing vehicles, updating operational statuses).

---

## Constraint-Based Optimization

Manual assignment of crew shifts and daily bus dispatches leads to resource conflicts, contract violations, and unbalanced workloads. RouteNetLK uses **Timefold Solver 1.32.0** (`ai.timefold.solver`) to solve two NP-hard combinatorial problems.

<p align="center">
  <img src="https://github.com/user-attachments/assets/9501888c-ea01-42a3-b4ff-25269b63d052" width="250" alt="Timefold Solver Architecture">
</p>

### 1. Crew Shift Rostering (`module/roster/planner`)
- **Planning Entity:** `RosterShiftAssignmentPlanning`
- **Planning Variable:** `EmployeeFact` (supplied via `@ValueRangeProvider(id = "employeeRange")`)
- **Hard Constraints ([`RosterConstraintProvider`](src/main/java/lk/ashan/routenetlkserverapllication/module/roster/planner/RosterConstraintProvider.java)):**
  - `requiredDesignation` / `designationMatch`: Penalizes assigning an employee whose designation does not match the shift role.
  - `noOverlappingShifts`: Penalizes assigning the same employee to overlapping shift intervals on the same date.
  - `oneDriverOneConductorPerShift`: Penalizes shifts that do not have distinct driver and conductor assignments.
- **Soft Constraints:**
  - `fairWorkloadDistribution`: Groups assignments by employee and penalizes the square of shift counts ($count^2$), driving the solver toward an even distribution.
---

## Security Architecture

The backend implements stateless security using **Spring Security 6** and **JSON Web Tokens (JJWT 0.11.5)**.

<p align="center">
  <img src="https://github.com/user-attachments/assets/a48e66f8-a381-4ce5-abd6-9bc93c967e7d" width="200" height="806" alt="Backend Security Architecture">
</p>

### Security Highlights:
- **Brute-Force Lockout Defense:** [`MyAuthenticationProvider`](src/main/java/lk/ashan/routenetlkserverapllication/security/MyAuthenticationProvider.java) and [`LoginAttemptService`](src/main/java/lk/ashan/routenetlkserverapllication/security/LoginAttemptService.java) track failed attempts using a thread-safe Google Guava `LoadingCache`. Accounts/IPs exceeding 5 consecutive failed attempts are automatically locked for 15 minutes.
- **Stateless Session Management:** Configured with `SessionCreationPolicy.STATELESS`. No server-side session state or cookies are stored.
- **Granular RBAC and PBAC:** Access is evaluated using both high-level roles (e.g., `ROLE_DEPOT_MANAGER`, `ROLE_INVENTORY_OFFICER`, `ROLE_SYSTEM_ADMIN`) and granular action privileges (e.g., `READ_VEHICLE`, `CREATE_TRIP`).
- **Security Headers:** Enforces Content Security Policy (CSP), `X-XSS-Protection`, and `X-Frame-Options: SAMEORIGIN`.
- **CORS Configuration:** Explicit origin, method, and header white-listing via `UrlBasedCorsConfigurationSource`.

---

## API Architecture

REST endpoints follow standard HTTP semantics and return unified JSON response wrappers.

### Standard Response Envelopes

#### Success Response (`APISuccessResponse<T>`)
```json
{
  "status": "SUCCESS",
  "message": "Operation completed successfully",
  "data": { ... },
  "timestamp": "2026-08-21T11:27:36"
}
```

#### Error Response (`APIErrorResponse`)
```json
{
  "status": "ERROR",
  "errorCode": "RULE_VIOLATION",
  "message": "Invalid combination: Type C buses cannot be used on Inter Provincial route.",
  "timestamp": "2026-08-21T11:27:36"
}
```

### Global Exception Translation
Centralized in [`GlobalExceptionHandler`](src/main/java/lk/ashan/routenetlkserverapllication/shared/exception/GlobalExceptionHandler.java), domain exceptions map directly to HTTP statuses and distinct error codes:

| Exception | HTTP Status | Error Code |
|---|---|---|
| `ResourceNotFoundException` | `404 NOT FOUND` | `RESOURCE_NOT_FOUND` |
| `ResourceExistsException` | `409 CONFLICT` | `RESOURCE_EXISTS` |
| `BusinessRuleViolationException` | `422 UNPROCESSABLE_ENTITY` | `RULE_VIOLATION` |
| `InvalidStateTransitionException` | `400 BAD REQUEST` | `INVALID_STATE_TRANSITION` |
| `MethodArgumentNotValidException` | `400 BAD REQUEST` | `VALIDATION_FAILED` |
| `BadCredentialsException` | `401 UNAUTHORIZED` | `AUTHENTICATION_FAILED` |
| `LockedException` | `423 LOCKED` | `ACCOUNT_LOCKED` |

---

## Persistence & Data Isolation

Data persistence is built on **Spring Data JPA** and **Hibernate ORM** targeting **MySQL 8**.

<p align="center">
  <img src="https://github.com/user-attachments/assets/2cdf1d40-61e0-4281-8cc6-bca6da3658d1" width="500" height="235" alt="Persistence Data Isolation">
</p>

### Multi-Tenant Branch Scoping via Aspect-Oriented Programming (AOP)
To guarantee that depot officers can only query data belonging to their assigned branch, [`BranchAndUserFilterAspect`](src/main/java/lk/ashan/routenetlkserverapllication/shared/transaction/BranchAndUserFilterAspect.java) intercepts service query executions (`get*` and `search*` methods):
- Extracts the authenticated user's `branchId` from `CustomUserPrincipal`.
- Automatically activates Hibernate's `branchFilter` on the underlying database session.
- System administrators bypass this filter automatically.
- Service methods requiring cross-branch visibility can selectively disable the filter using `@DisableBranchFilter`.

### Transparent Soft-Deletion
All domain entities inherit from [`BaseEntity`](src/main/java/lk/ashan/routenetlkserverapllication/shared/model/BaseEntity.java), which defines Hibernate `@FilterDef` and `@Filter(name = "softDeleteFilter", condition = "deleted = :is_deleted")`. The [`SoftDeleteFilterAspect`](src/main/java/lk/ashan/routenetlkserverapllication/shared/transaction/SoftDeleteFilterAspect.java) ensures deleted records are omitted by default, while `@DisableSoftDeleteFilter` allows audit queries to inspect historical records.

---

## Cross-Cutting Infrastructure

Located under `lk.ashan.routenetlkserverapllication.shared`:

- **Automated Sequential Number Generator (`shared/numbergenerator`):** Generates standardized reference codes (e.g., `PRM-CLM-0001`, `TRP-2026-0042`) using database-backed sequence records (`DocSequence`) categorized by scope and code type.
- **Asynchronous Templated Email (`shared/email`):** Non-blocking email dispatch using `JavaMailSender` and Thymeleaf HTML templates (`classpath:/templates/email/`).
- **JPA Entity Auditing (`shared/audit`):** Tracks record modification metadata using `AuditorAwareImpl`.
- **Centralized Regex Provider (`shared/validation`):** Provides verified regex patterns for Sri Lankan telephone numbers, NIC formats, and vehicle registration plates.

---

## Event-Driven Decoupling

Selected cross-domain interactions use Spring Application Events (`ApplicationEventPublisher`) to decouple core business transactions from secondary side effects.

<p align="center">
  <img src="https://github.com/user-attachments/assets/1064185c-e89e-4df9-b7de-b3a489279c42" width="500" alt="Event-Driven Decoupling Architecture">
</p>

### Verified Application Events:
- **`PermitTransferredEvent`:** Handled by [`VehiclePermitEventListener`](src/main/java/lk/ashan/routenetlkserverapllication/module/vehicle/event/VehiclePermitEventListener.java) to release the bus back into depot availability, and by [`NotificationEventDispatcher`](src/main/java/lk/ashan/routenetlkserverapllication/shared/notification/listener/NotificationEventDispatcher.java) to alert the depot manager.
- **`FareReconciledEvent`:** Triggers depot manager confirmation alerts upon daily revenue reconciliation.
- **`PartRequestApprovedEvent`:** Alerts inventory officers to prepare spare parts for maintenance.
- **`PartReceivedEvent`:** Notifies maintenance mechanics that requested parts have been received.

---

## Testing Strategy

The backend maintains an automated testing suite covering unit logic, optimization scoring, web contracts, and relational persistence.

<p align="center">
  <img src="https://github.com/user-attachments/assets/05cbaeb7-7496-4278-816b-268316384fbd" width="250" alt="Testing Strategy Overview">
</p>

### Testing Infrastructure:
- **Real MySQL Testing via Testcontainers:** Repository integration tests extend [`BaseTestContainer`](src/test/java/lk/ashan/routenetlkserverapllication/shared/config/BaseTestContainer.java), which spins up a dedicated `mysql:8.3.0` Docker container dynamically. This validates custom SQL queries, native dialect functions, and soft-deletion filters against real MySQL rather than in-memory emulators like H2.
- **Constraint Scoring Verification:** Timefold planning rules are verified using `ConstraintVerifier` (`RosterConstraintProviderTest`, `TripExecutionConstraintProviderTest`) to prove penalization of overlapping shifts and fair workload distributions.
- **Web Layer Testing:** Controller endpoints are tested using `@WebMvcTest` and `MockMvc` with active security filters (`TestSecurityConfiguration`).

---

## CI/CD Pipeline

Automated deployment is configured using GitHub Actions ([`.github/workflows/deploy.yml`](.github/workflows/deploy.yml)):

<p align="center">
  <img src="https://github.com/user-attachments/assets/e3bce14f-194b-4ba2-9bed-cfdfed1b8e44" width="250" alt="CI/CD Pipeline Flow">
</p>

1. **Build & Package:** Sets up Docker Buildx and compiles the image with GitHub Actions layer caching (`type=gha`).
2. **Registry Publication:** Pushes dual tags (`latest` and `${{ github.sha }}`) to Docker Hub.
3. **Targeted Deployment:** Connects to the AWS EC2 instance via SSH (`appleboy/ssh-action@v1.0.3`), pulls the updated image, and recreates only the backend container (`docker compose up -d --no-deps backend`) without disturbing other services.

---

## Technology Stack

| Category | Technologies / Libraries | Purpose |
|---|---|---|
| **Core Framework** | Java 17, Spring Boot 3.5.3, Spring MVC | Enterprise runtime and application framework |
| **Persistence & ORM** | Spring Data JPA, Hibernate ORM, MySQL Connector (8.0.33) | Relational persistence and query abstraction |
| **Optimization** | Timefold Solver 1.32.0 (`timefold-solver-spring-boot-starter`) | Constraint satisfaction solver for roster & dispatch |
| **Security** | Spring Security 6, JJWT (0.11.5), Google Guava (31.1-jre) | Stateless JWT authentication, RBAC, brute-force lockout |
| **Mapping & Boilerplate**| MapStruct 1.5.5.Final, Project Lombok 1.18.30 | Compile-time DTO-entity mapping and boilerplate reduction |
| **Notifications & Mail** | Spring Boot Mail, Thymeleaf | HTML templated transactional email notifications |
| **Testing** | JUnit 5, Mockito, Spring Security Test, Testcontainers MySQL (8.3.0), Timefold Test | Multi-tier unit, constraint, web, and database testing |
| **Containerization** | Docker (Multi-stage, Layer-extracted, Alpine JRE) | Lightweight, non-root runtime containerization |
| **CI/CD** | GitHub Actions, Docker Hub, SSH Action | Automated container build, publish, and EC2 deployment |

---

## Project Structure

```text
RouteNetLKServerApplication/
├── .github/
│   └── workflows/
│       └── deploy.yml            # CI/CD automated build & SSH deployment pipeline
├── src/
│   ├── main/
│   │   ├── java/lk/ashan/routenetlkserverapllication/
│   │   │   ├── RouteNetLKServerApplication.java
│   │   │   ├── dashboard/        # Operational KPIs and dashboard metrics
│   │   │   ├── module/           # Domain modules (Branch, Crew, Trip, Roster, etc.)
│   │   │   ├── report/           # Multi-depot analytics and reporting projections
│   │   │   ├── security/         # Spring Security, JWT filters, authentication providers
│   │   │   └── shared/           # AOP filters, exception handling, email, auditing
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/email/  # Thymeleaf HTML email templates
│   └── test/
│       └── java/lk/ashan/routenetlkserverapllication/
│           ├── module/           # Unit, repository, controller, and constraint tests
│           └── shared/config/    # BaseTestContainer (MySQL) and TestSecurityConfiguration
├── Dockerfile                    # Multi-stage layer-extracted container definition
├── pom.xml                       # Maven build configuration and dependency declarations
└── README.md                     # Root project documentation
```

---

## Local Development Setup

### Prerequisites
- **Java Development Kit (JDK):** Version 17+
- **Apache Maven:** Version 3.8+ (or use the included `./mvnw` wrapper)
- **MySQL Database Server:** Version 8.x
- **Docker:** (Optional, required if executing Testcontainers integration tests)

### 1. Clone the Repository
```bash
git clone https://github.com/Ashan-Dissanayake/RouteNetLKServerApplication.git
cd RouteNetLKServerApplication
```

### 2. Configure Database & Properties
Create a local MySQL database:
```sql
CREATE DATABASE routenetlk CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Update your `src/main/resources/application.properties` (or set environment variables):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/routenetlk?createDatabaseIfNotExist=true&useSSL=false
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

jwt.secret=YOUR_BASE64_ENCODED_256_BIT_SECRET_KEY
jwt.expiration=86400000

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL@gmail.com
spring.mail.password=YOUR_APP_SPECIFIC_PASSWORD
```

### 3. Build the Application
```bash
# Using Maven wrapper (Linux / macOS)
./mvnw clean package

# Using Maven wrapper (Windows)
mvnw.cmd clean package
```

### 4. Run the Test Suite
```bash
./mvnw test
```

### 5. Start the Server
```bash
./mvnw spring-boot:run
```
The server will start on port `8080` (accessible at `http://localhost:8080`).

---

## Related Repositories

- **[RouteNetLK System Overview](https://github.com/Ashan-Dissanayake/RouteNetLK):** Main system documentation, full microservices/monolith deployment architecture, AWS infrastructure with Terraform, and end-to-end user workflows.
- **[RouteNetLK Client Application](https://github.com/Ashan-Dissanayake/RouteNetLKClientApplication):** Angular 19 single-page application providing responsive depot management interfaces, scheduling boards, and live analytics.

---

## Engineering Highlights

- **Constraint Optimization:** Solves complex combinatorial driver and vehicle assignments using Timefold Solver, optimizing hard legal constraints and soft fairness metrics.
- **Design Pattern Discipline:** Applies the Strategy Pattern for modular business validation pipelines and the State Pattern for deterministic lifecycle state management.
- **AOP-Driven Multi-Tenancy:** Employs Aspect-Oriented Programming and Hibernate filters for automatic branch-level data scoping and transparent soft-deletion.
- **High-Integrity Testing:** Employs Testcontainers MySQL 8.3.0 to validate persistence, transactions, and custom query behavior against real database engines.
- **Defensive Security:** Implements JWT stateless authorization with automatic brute-force lockout defenses backed by thread-safe Guava caches.
- **Optimized Containerization:** Multi-stage, layer-extracted Docker build with JVM memory tuning tailored for resource-efficient cloud execution.

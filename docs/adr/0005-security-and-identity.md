# ADR 0005: Security and Identity Decisions

## Status
Accepted

## Context
The RouteNetLK Server Application handles sensitive transportation data and administrative functions. We need a secure, scalable, and stateless authentication and authorization system to protect resources and manage user identities across different branches and roles.

## Decision

We have implemented a multi-layered security architecture using **Spring Security 6.x** and **JWT (JSON Web Token)**.

### 1. Stateless Authentication (JWT)
- **Mechanism:** We use JWT for stateless authentication. 
- **Tooling:** `io.jsonwebtoken` (jjwt) library.
- **Process:**
    - `AuthenticationFilter` intercepts login requests (`/login`).
    - Upon successful authentication, a JWT is generated containing the username and authorities (roles/privileges).
    - The token is returned in the `Authorization` header (`Bearer <token>`).
- **Token Properties:** Secret key and expiration duration are configurable via `application.properties`.

### 2. Custom Authentication Logic
- **Authentication Provider:** `MyAuthenticationProvider` implements `AuthenticationProvider` to handle custom logic.
- **Brute-Force Protection:**
    - **Username-based Lock:** Uses a `LoadingCache` (Google Guava) to track failed attempts per username. Temporarily locks the account after 3 failed attempts for 5 seconds (configurable).
    - **IP-based Blocking:** `LoginAttemptService` tracks failed attempts per IP address. Blocks an IP for 15 minutes after 5 failed attempts (configurable).
- **Dual User Source:** Supports both an in-memory admin user (for bootstrap/testing) and database-stored users.

### 3. Granular Authorization
- **Method-Level Security:** Enabled via `@EnableMethodSecurity`. We use `@PreAuthorize` annotations on controller methods.
- **Privilege-based Access:** Authorities are structured as `module-operation` (e.g., `branch-add`, `vehicle-view`).
- **Role-based Access:** Standard Spring Security roles prefixed with `ROLE_` (e.g., `ROLE_SYSTEM_ADMIN`, `ROLE_DEPOT_MANAGER`).
- **Authorization Filter:** `AuthorizationFilter` intercepts every request, validates the JWT, and populates the `SecurityContextHolder` with a `CustomUserPrincipal`.

### 4. User Identity Management
- **User Details:** `MyUserDetailsService` loads users from the `UserRepository`.
- **Custom Principal:** `CustomUserPrincipal` wraps the domain `User` entity, providing easy access to user details (like `branchId`) throughout the application.
- **Account Status:** Supports checking for permanent account locks managed by administrators.

### 5. Web Security Configurations
- **CORS:** Configured to allow specific origins (e.g., Angular frontend at `http://localhost:4200`) and standard HTTP methods.
- **CSRF:** Disabled as the API is stateless (JWT-based).
- **Security Headers:** Enforced via `SecurityConfiguration`:
    - Content Security Policy (CSP).
    - X-XSS-Protection.
    - X-Frame-Options (SameOrigin).
- **Exception Handling:** `CustomAccessDeniedHandler` and a custom `AuthenticationEntryPoint` ensure standardized error responses for security violations.

## Consequences

- **Pros:**
    - **Scalability:** Stateless JWT allows the application to scale horizontally without session synchronization issues.
    - **Security:** Built-in protection against brute-force attacks at both username and IP levels.
    - **Flexibility:** Granular privilege-based authorization allows for fine-tuned access control.
    - **Standardization:** Uses industry-standard Spring Security patterns.
- **Cons:**
    - **Token Revocation:** Stateless JWTs are difficult to revoke before they expire (mitigated by short expiration times).
    - **Complexity:** Managing both roles and granular privileges adds some complexity to the authorization logic.
    - **In-memory Cache:** Brute-force protection caches are currently in-memory, which wouldn't be shared across multiple server instances (could be moved to Redis if needed).

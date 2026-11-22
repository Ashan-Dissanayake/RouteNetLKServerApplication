# ADR 0009: Observability and Monitoring Decisions

## Status
Accepted

## Context
As the RouteNetLK system grows, it becomes critical to monitor its health, performance, and behavior in real-time to detect issues before they affect users.

## Decision
We have adopted a standardized approach to logging, metrics, and error reporting.

### 1. Standardized Logging
- **Library:** We use **SLF4J** with **Logback** (Spring Boot defaults) for all logging.
- **Pattern:** We use Lombok's `@Slf4j` annotation in classes to minimize boilerplate.
- **Content:** Logs include correlation information (where possible) and distinguish between different levels (`INFO` for significant lifecycle events, `WARN` for recoverable issues, and `ERROR` for failures requiring attention).
- **Security:** We strictly avoid logging sensitive information such as passwords, JWT tokens, or full PII (Personally Identifiable Information) in logs.

### 2. Structured Error Reporting
- **Error Codes:** We use a custom `ErrorCode` enum that maps business errors to HTTP statuses and unique machine-readable codes.
- **RFC 7807 Compliance:** Our error responses follow the **Problem Details for HTTP APIs (RFC 7807)** standard, including a `typeUri` that points to documentation for each specific error type.

### 3. Application Health and Metrics
- **Actuator:** We use **Spring Boot Actuator** to expose health indicators (`/health`) and basic metrics (`/metrics`).
- **Health Indicators:** Custom health checks (e.g., database connectivity, mail server status) are exposed to allow external monitoring tools (like UptimeRobot or Kubernetes Liveness/Readiness probes) to track system status.

### 4. Auditing as Monitoring
- **JPA Auditing:** While primarily for data integrity, the `BaseEntity` auditing provides a trail of system activity that can be used for forensic analysis of user actions and system changes.

## Consequences

- **Pros:**
    - **Visibility:** Provides a clear view into what the system is doing and where errors are occurring.
    - **Standardization:** Consistent error formats and logging levels simplify log aggregation and analysis.
    - **Integration:** Standard health endpoints make it easy to integrate with infrastructure monitoring tools.
- **Cons:**
    - **Log Volume:** Excessive logging at the `INFO` level can lead to high storage costs and "noise" that hides important issues.
    - **Manual Effort:** Ensuring that every new service or component has proper logging and custom health checks requires ongoing developer diligence.

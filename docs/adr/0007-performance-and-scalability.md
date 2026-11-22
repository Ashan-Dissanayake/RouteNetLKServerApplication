# ADR 0007: Performance and Scalability Decisions

## Status
Accepted

## Context
The RouteNetLK Server Application handles resource-intensive tasks like roster scheduling and real-time conflict detection. It is also intended to run on cost-effective infrastructure (e.g., AWS t3.micro). We need to ensure the system remains performant and scalable within these constraints.

## Decision
We have implemented several strategies to optimize performance and ensure the application can scale horizontally.

### 1. Algorithm Optimization
- **Overlap Detection:** We use a custom **Interval Tree** implementation for detecting scheduling conflicts (e.g., overlapping shifts or vehicle assignments). This reduces the complexity of overlap checks from O(N) to **O(log N)**, which is critical for large datasets.
- **Optimization Solver:** We use **Timefold Solver** for complex rostering and route optimization, which employs advanced meta-heuristics to find high-quality solutions efficiently without exhaustive searching.

### 2. Runtime Performance (JVM Tuning)
- **Memory Management:** The JVM is configured with specific flags for constrained environments (e.g., `-Xms128m -Xmx256m`).
- **Garbage Collection:** We use **SerialGC** (`-XX:+UseSerialGC`) for low-resource environments where single-core performance is prioritized over multi-core throughput.
- **Fail-Fast:** The `-XX:+ExitOnOutOfMemoryError` flag is used to ensure the container terminates and restarts cleanly in case of fatal memory issues.

### 3. Horizontal Scalability
- **Stateless Design:** The application is strictly stateless. All session information is handled via JWT tokens, allowing any instance of the application to handle any request.
- **Containerization:** The application is packaged as a Docker image, enabling easy scaling across multiple nodes using orchestrators like Kubernetes or AWS ECS.

### 4. Database Optimization
- **Indexing:** Standard JPA/Hibernate indexing is applied to frequently queried columns (e.g., codes, emails, and foreign keys).
- **Soft Delete Filtering:** Global Hibernate filters ensure that deleted records are excluded at the database level, preventing unnecessary data transfer and processing in the application layer.

## Consequences

- **Pros:**
    - **Efficiency:** O(log N) algorithms ensure the system stays fast even as data grows.
    - **Cost-Effective:** Low memory footprint allows running on cheap cloud instances.
    - **Reliability:** Automated restarts on OOM errors prevent "zombie" processes.
- **Cons:**
    - **Complexity:** Custom data structures like Interval Trees require more maintenance than standard collections.
    - **Tuning:** JVM memory limits must be carefully monitored to avoid frequent restarts if the load increases significantly.

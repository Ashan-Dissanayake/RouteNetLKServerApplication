# ADR 0006: Integration and Communication Decisions

## Status
Accepted

## Context
The RouteNetLK Server Application needs to communicate with front-end applications (e.g., Angular), external services, and users. We need to define standardized protocols, message formats, and communication strategies to ensure consistency, reliability, and ease of integration.

## Decision

We have adopted a combination of synchronous RESTful APIs for client-server communication and asynchronous messaging for notifications (email).

### 1. Synchronous Communication (REST APIs)
- **Protocol:** We use **REST (Representational State Transfer)** over HTTPS.
- **Data Format:** **JSON** is the primary format for all request and response payloads.
- **Documentation:** APIs are automatically documented using **SpringDoc OpenAPI (Swagger)**, providing a clear contract for frontend developers.
- **Standardized Response Structure:** All successful API responses follow a consistent envelope pattern using `APISuccessResponse<T>`:
    - `data`: The actual payload (object or collection).
    - `meta`: Metadata such as pagination info or timestamps.
    - `links`: HATEOAS-style links for related resources (where applicable).

### 2. Error Handling & Communication
- **Global Exception Handling:** A `GlobalExceptionHandler` ensures that all errors across the system result in a standardized error response.
- **Error Format:** Errors include a machine-readable error code, a human-readable message, and validation details (if applicable), preventing sensitive stack traces from leaking.

### 3. Asynchronous Communication (Email Notifications)
- **Mechanism:** We use Spring's **JavaMailSender** for sending emails asynchronously (where configured) to prevent blocking the main execution thread.
- **Implementation:** `EmailSenderService` provides a unified interface for sending emails.
- **Templates:** **Thymeleaf** is used as the template engine for HTML emails, allowing for dynamic content generation and consistent branding.
- **Use Cases:** Used for password resets, system alerts, and notification of administrative actions.

### 4. Cross-Origin Resource Sharing (CORS)
- **Policy:** As the frontend and backend are hosted on different origins, CORS is strictly configured in `SecurityConfiguration` to allow only authorized origins (e.g., specific production domains and `http://localhost:4200` for development).

### 5. Future-Proofing for External Integrations
- **Modular Design:** Integration points (like email) are abstracted via service interfaces, making it easy to swap implementations (e.g., moving from SMTP to a service like SendGrid or AWS SES) without affecting business logic.

## Consequences

- **Pros:**
    - **Consistency:** Frontend developers can rely on a predictable API structure.
    - **Developer Experience:** Interactive Swagger UI simplifies API testing and discovery.
    - **User Experience:** Asynchronous email ensures the UI remains responsive during long-running tasks.
    - **Security:** Standardized error handling and strict CORS policies protect the system from information leakage and unauthorized access.
- **Cons:**
    - **Overhead:** Wrapping simple responses in an envelope (`data`, `meta`) adds slight verbosity.
    - **Maintenance:** Email templates need to be maintained separately from the code.
    - **SMTP Dependency:** Synchronous email sending (if not moved to a task executor) could still impact performance if the SMTP server is slow.

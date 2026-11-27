package lk.ashan.routenetlkserverapllication.shared.logging;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Persistent audit log entry.
 *
 * <p>Records security-relevant and business-critical events such as
 * login successes/failures, authorisation denials, and data mutations.
 * Each entry captures who did what, from where, and whether it succeeded.</p>
 */
@Entity
@Table(
        name = "auditlog",
        indexes = {
                @Index(name = "idx_audit_username",   columnList = "username"),
                @Index(name = "idx_audit_action",     columnList = "action"),
                @Index(name = "idx_audit_created_at", columnList = "created_at")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Authenticated username, or "anonymous" for unauthenticated requests. */
    @Column(name = "username")
    private String username;

    /**
     * High-level action category.
     * Examples: LOGIN_SUCCESS, LOGIN_FAILURE, ACCESS_DENIED,
     *           CREATE, UPDATE, DELETE, EXPORT.
     */
    @Column(name = "action", nullable = false)
    private String action;

    /**
     * Resource type involved in the action.
     * Example: "User", "Vehicle", "Trip".
     */
    @Column(name = "resourcetype")
    private String resourceType;

    /**
     * Identifier of the specific resource instance affected.
     * Example: entity ID or document number.
     */
    @Column(name = "resourceid")
    private String resourceId;

    /** Short human-readable description of the event. */
    @Column(name = "description")
    private String description;

    /** Client IP address at the time of the action. */
    @Column(name = "ipaddress")
    private String ipAddress;

    /** HTTP method of the triggering request (GET, POST, PUT, DELETE…). */
    @Column(name = "httpmethod")
    private String httpMethod;

    /** Request URI of the triggering request. */
    @Column(name = "requesturi")
    private String requestUri;

    /** Whether the action completed successfully. */
    @Column(name = "success", nullable = false)
    private boolean success;

    /** Timestamp when the event occurred (set automatically on insert). */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

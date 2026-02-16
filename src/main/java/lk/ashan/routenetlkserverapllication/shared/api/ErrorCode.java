package lk.ashan.routenetlkserverapllication.shared.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // ==================== CLIENT ERRORS (4xx) ====================

    /**
     * Bean validation failures (@Valid, @NotNull, etc.)
     * Use for: DTO validation errors, constraint violations
     */
    VALIDATION_FAILED(
            "Validation failed",
            HttpStatus.BAD_REQUEST,
            "/errors/validation-failed"
    ),

    /**
     * Requested resource doesn't exist
     * Use for: Entity not found in database
     */
    RESOURCE_NOT_FOUND(
            "Resource not found",
            HttpStatus.NOT_FOUND,
            "/errors/resource-not-found"
    ),

    /**
     * Resource already exists (unique constraint violation)
     * Use for: Duplicate email, employee number, vehicle number, etc.
     */
    RESOURCE_ALREADY_EXISTS(
            "Resource already exists",
            HttpStatus.CONFLICT,
            "/errors/resource-already-exists"
    ),

    /**
     * Business rule violation
     * Use for: Domain logic violations (NIC gender mismatch, invalid department-designation, etc.)
     */
    BUSINESS_RULE_VIOLATION(
            "Business rule violation",
            HttpStatus.CONFLICT,
            "/errors/business-rule-violation"
    ),

    /**
     * Invalid state transition in state machine
     * Use for: Trip status transitions, permit status changes, etc.
     */
    INVALID_STATE_TRANSITION(
            "Invalid state transition",
            HttpStatus.CONFLICT,
            "/errors/invalid-state-transition"
    ),

    /**
     * General bad request for malformed data
     * Use for: Invalid JSON, type mismatches
     */
    BAD_REQUEST(
            "Bad request",
            HttpStatus.BAD_REQUEST,
            "/errors/bad-request"
    ),

    // ==================== SERVER ERRORS (5xx) ====================

    /**
     * Unexpected internal error
     * Use for: NPE, database errors, unexpected runtime exceptions
     */
    INTERNAL_ERROR(
            "Internal server error",
            HttpStatus.INTERNAL_SERVER_ERROR,
            "/errors/internal-error"
    ),

    /**
     * External service unavailable
     * Use for: Database down, external API failures
     */
    SERVICE_UNAVAILABLE(
            "Service unavailable",
            HttpStatus.SERVICE_UNAVAILABLE,
            "/errors/service-unavailable"
    );

    private final String title;
    private final HttpStatus status;
    private final String typeUri;
}


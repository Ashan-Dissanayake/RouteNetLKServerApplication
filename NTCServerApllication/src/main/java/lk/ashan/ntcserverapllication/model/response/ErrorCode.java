package lk.ashan.ntcserverapllication.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 4xx: Client Errors (Business-Level)
    ID_MISMATCH("ID mismatch", HttpStatus.BAD_REQUEST, "/errors/id-mismatch"),
    RESOURCE_NOT_FOUND("Resource not found", HttpStatus.NOT_FOUND, "/errors/resource-not-found"),
    RESOURCE_ALREADY_EXISTS("Resource already exists", HttpStatus.CONFLICT, "/errors/resource-already-exists"),
    INVALID_DATA("Invalid input data", HttpStatus.BAD_REQUEST, "/errors/invalid-data"),
    STATUS_INVALID("Invalid status", HttpStatus.BAD_REQUEST, "/errors/status-invalid"),
    CONTACT_INVALID("Invalid contact information", HttpStatus.BAD_REQUEST, "/errors/contact-invalid"),

    // 5xx: Server Errors (Technical-Level)
    SERVICE_UNAVAILABLE("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE, "/errors/service-unavailable"),
    DATA_CONFLICT("Data conflict", HttpStatus.INTERNAL_SERVER_ERROR, "/errors/data-conflict"),
    UNKNOWN_ERROR("Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR, "/errors/unknown-error");

    private final String title;
    private final HttpStatus status;
    private final String typeUri;
}

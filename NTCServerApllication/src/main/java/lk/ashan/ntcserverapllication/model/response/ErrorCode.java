package lk.ashan.ntcserverapllication.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Business-Level Error Codes (4xx)
    EMPLOYEE_ID_MISMATCH("Employee ID mismatch", HttpStatus.BAD_REQUEST, "/errors/employee-id-mismatch"),
    EMPLOYEE_NOT_FOUND("Employee not found", HttpStatus.NOT_FOUND, "/errors/employee-not-found"),
    EMPLOYEE_ALREADY_EXISTS("Employee already exists", HttpStatus.CONFLICT, "/errors/employee-already-exists"),
    INVALID_EMPLOYEE_DATA("Invalid employee data", HttpStatus.BAD_REQUEST, "/errors/invalid-employee-data"),
    EMPLOYEE_STATUS_INVALID("Invalid employee status", HttpStatus.BAD_REQUEST, "/errors/employee-status-invalid"),
    EMPLOYEE_CONTACT_INVALID("Invalid contact information", HttpStatus.BAD_REQUEST, "/errors/employee-contact-invalid"),

    // Technical-Level Error Codes (5xx)
    EMPLOYEE_SERVICE_UNAVAILABLE("Employee service unavailable", HttpStatus.SERVICE_UNAVAILABLE, "/errors/employee-service-unavailable"),
    EMPLOYEE_DATA_CONFLICT("Employee data conflict", HttpStatus.INTERNAL_SERVER_ERROR, "/errors/employee-data-conflict"),
    UNKNOWN_ERROR("Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR, "/errors/employee-unknown-error");

    private final String title;
    private final HttpStatus status;
    private final String typeUri;
}

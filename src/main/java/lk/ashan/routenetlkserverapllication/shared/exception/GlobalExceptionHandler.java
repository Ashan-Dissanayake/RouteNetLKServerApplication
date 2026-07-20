package lk.ashan.routenetlkserverapllication.shared.exception;

import jakarta.servlet.http.HttpServletRequest;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APIErrorResponse;
import lk.ashan.routenetlkserverapllication.shared.api.ErrorCode;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<APIErrorResponse> handleBusinessException(
            BusinessException e,
            HttpServletRequest request) {

        log.warn("Business exception: {} - {}", e.getErrorCode(), e.getMessage());

        return APIResponseBuilder.error(
                e.getErrorCode(),
                List.of(e.getMessage()),
                request
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIErrorResponse> handleValidationException(
            MethodArgumentNotValidException e,
            HttpServletRequest request) {

        List<String> details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        log.warn("Validation failed: {}", details);

        return APIResponseBuilder.error(
                ErrorCode.VALIDATION_FAILED,
                details,
                request
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIErrorResponse> handleAccessDeniedException(
            AccessDeniedException e,
            HttpServletRequest request) {

        return APIResponseBuilder.error(
                ErrorCode.ACCESS_DENIED,
                List.of("You do not have permission to perform this operation."),
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIErrorResponse> handleGenericException(
            Exception e,
            HttpServletRequest request) {

        log.error("Unexpected error occurred", e);

        return APIResponseBuilder.error(
                ErrorCode.INTERNAL_ERROR,
                List.of("An unexpected error occurred,"+e.getMessage()),
                request
        );
    }
}

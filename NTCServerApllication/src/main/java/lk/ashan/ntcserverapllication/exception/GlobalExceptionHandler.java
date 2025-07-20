package lk.ashan.ntcserverapllication.exception;

import jakarta.servlet.http.HttpServletRequest;
import lk.ashan.ntcserverapllication.paylaod.response.APIErrorResponse;
import lk.ashan.ntcserverapllication.enums.ErrorCode;
import lk.ashan.ntcserverapllication.util.APIResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<APIErrorResponse> handleExistsException(
            ResourceExistsException e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.RESOURCE_ALREADY_EXISTS,
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIErrorResponse> handleNotFoundException(
            ResourceNotFoundException e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.RESOURCE_NOT_FOUND,
                e.getMessage(),
                request
        );
    }

    // Add generic fallback for unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIErrorResponse> handleGenericException(
            Exception e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.UNKNOWN_ERROR,
                e.getMessage(),
                request
        );
    }
}

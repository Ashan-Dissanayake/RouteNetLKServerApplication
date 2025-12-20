package lk.ashan.routenetlkserverapllication.shared.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APIErrorResponse;
import lk.ashan.routenetlkserverapllication.shared.api.ErrorCode;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<APIErrorResponse> handleExistsException(
            ResourceExistsException e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.RESOURCE_ALREADY_EXISTS,
                List.of(e.getMessage()),
                request
        );
    }

    @ExceptionHandler(ContactConflictException.class)
    public ResponseEntity<APIErrorResponse> handleContactConflictException(
            ContactConflictException e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.DATA_CONFLICT,
                List.of(e.getMessage()),
                request
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<APIErrorResponse> handleContactConflictException(
            ValidationException e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.DATA_CONFLICT,
                List.of(e.getMessage()),
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
                List.of(e.getMessage()),
                request
        );
    }

    @ExceptionHandler(InvalidNICGenderException.class)
    public ResponseEntity<APIErrorResponse> handleInvalidGenderNicException(
            InvalidNICGenderException e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.DATA_CONFLICT,
                List.of(e.getMessage()),
                request
        );
    }

    @ExceptionHandler(InvalidDepartmentDesignationException.class)
    public ResponseEntity<APIErrorResponse> handleInvalidDepartmentDesignationException(
            InvalidDepartmentDesignationException e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.DATA_CONFLICT,
                List.of(e.getMessage()),
                request
        );
    }

    @ExceptionHandler(InvalidGenderDesignationException.class)
    public ResponseEntity<APIErrorResponse> handleInvalidGenderDesignationException(
            InvalidGenderDesignationException e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.DATA_CONFLICT,
                List.of(e.getMessage()),
                request
        );
    }

    @ExceptionHandler(BusinessRuleValidationException.class)
    public ResponseEntity<APIErrorResponse> handleBusinessRuleValidationException(
            BusinessRuleValidationException e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.INVALID_DATA,
                List.of(e.getMessage()),
                request
        );
    }

    @ExceptionHandler(InvalidEmploymentDateException .class)
    public ResponseEntity<APIErrorResponse> handleIInvalidEmploymentDateException(
            InvalidEmploymentDateException e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.DATA_CONFLICT,
                List.of(e.getMessage()),
                request
        );
    }

    @ExceptionHandler(InvalidStatusTransitionException .class)
    public ResponseEntity<APIErrorResponse> handleInvalidStatusTransitionException(
            InvalidStatusTransitionException e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.DATA_CONFLICT,
                List.of(e.getMessage()),
                request
        );
    }

    @ExceptionHandler(InvalidStatusException .class)
    public ResponseEntity<APIErrorResponse> handleInvalidStatusTransitionException(
            InvalidStatusException e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.STATUS_INVALID,
                List.of(e.getMessage()),
                request
        );
    }

    @ExceptionHandler(InvalidRateTransitionException .class)
    public ResponseEntity<APIErrorResponse> handleInvalidRateTransitionException(
            InvalidRateTransitionException e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.DATA_CONFLICT,
                List.of(e.getMessage()),
                request
        );
    }

@ExceptionHandler(InvalidSeatingCapacityException .class)
    public ResponseEntity<APIErrorResponse> handleInvalidSeatingCapacityException(
        InvalidSeatingCapacityException e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.DATA_CONFLICT,
                List.of(e.getMessage()),
                request
        );
    }

    @ExceptionHandler(InvalidMileageException .class)
    public ResponseEntity<APIErrorResponse> handleInvalidMileageException(
            InvalidMileageException e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.DATA_CONFLICT,
                List.of(e.getMessage()),
                request
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIErrorResponse> handleValidationException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        List<String> details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return APIResponseBuilder.error(
                ErrorCode.INVALID_DATA,
                details,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIErrorResponse> handleGenericException(
            Exception e,
            HttpServletRequest request
    ) {
        return APIResponseBuilder.error(
                ErrorCode.UNKNOWN_ERROR,
                List.of(e.getMessage()),
                request
        );
    }
}

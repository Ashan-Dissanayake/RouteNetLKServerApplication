package lk.ashan.routenetlkserverapllication.shared.exception;

import lk.ashan.routenetlkserverapllication.shared.api.ErrorCode;

public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(message, ErrorCode.BUSINESS_RULE_VIOLATION);
    }

    public ValidationException(String field, String reason) {
        super("Validation failed for " + field + ": " + reason,
                ErrorCode.BUSINESS_RULE_VIOLATION);
    }
}

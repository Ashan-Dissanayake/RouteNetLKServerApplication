package lk.ashan.routenetlkserverapllication.shared.exception;

import lk.ashan.routenetlkserverapllication.shared.api.ErrorCode;

public class BusinessRuleViolationException extends BusinessException {
    public BusinessRuleViolationException(String message) {
        super(message, ErrorCode.BUSINESS_RULE_VIOLATION);
    }
}

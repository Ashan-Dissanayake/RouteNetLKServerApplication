package lk.ashan.routenetlkserverapllication.shared.exception;

import lk.ashan.routenetlkserverapllication.shared.api.ErrorCode;

public class InvalidStateTransitionException extends BusinessException {

    public InvalidStateTransitionException(String message) {
        super(message, ErrorCode.INVALID_STATE_TRANSITION);
    }

    public InvalidStateTransitionException(String fromState, String toState) {
        super("Invalid transition from " + fromState + " to " + toState,
                ErrorCode.INVALID_STATE_TRANSITION);
    }
}

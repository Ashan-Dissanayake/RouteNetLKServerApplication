package lk.ashan.routenetlkserverapllication.shared.exception;

import lk.ashan.routenetlkserverapllication.shared.api.ErrorCode;

public class ResourceExistsException extends BusinessException {
    public ResourceExistsException(String message) {
        super(message, ErrorCode.RESOURCE_ALREADY_EXISTS);
    }
}

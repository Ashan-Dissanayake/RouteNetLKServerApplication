package lk.ashan.routenetlkserverapllication.shared.exception;

import lk.ashan.routenetlkserverapllication.shared.api.ErrorCode;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(message, ErrorCode.RESOURCE_NOT_FOUND);

          }
}

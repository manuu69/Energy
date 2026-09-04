package org.example.energy.common.exception.type;

import lombok.Getter;
import org.example.energy.common.exception.code.ErrorCode;

@Getter
public class BusinessRuleException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessRuleException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public BusinessRuleException(ErrorCode errorCode, String detail) {
        super(detail);
        this.errorCode = errorCode;
    }
}

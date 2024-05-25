package com.lucasalmd.transfer.domain.exceptions;

import com.lucasalmd.transfer.domain.errors.ErrorResponse;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

    private final transient ErrorResponse response;


    public BusinessException(ErrorMessage message) {
        this.response = message.getErrorResponse();
    }
}

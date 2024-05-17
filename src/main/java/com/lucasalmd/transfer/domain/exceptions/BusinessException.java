package com.lucasalmd.transfer.domain.exceptions;

import com.lucasalmd.transfer.domain.errors.ErrorResponse;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

    private final ErrorResponse response;


    public BusinessException(Message message) {
        this.response = message.getErrorResponse();
    }
}

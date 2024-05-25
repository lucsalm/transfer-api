package com.lucasalmd.transfer.controller.handler;

import com.lucasalmd.transfer.domain.errors.ErrorResponse;
import com.lucasalmd.transfer.domain.exceptions.BusinessException;
import com.lucasalmd.transfer.domain.exceptions.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Objects;

@RestControllerAdvice
public class TransferAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException exception) {
        final ErrorResponse response = exception.getResponse();
        return ResponseEntity.status(response.code().getStatus()).body(response);
    }

    @ExceptionHandler({WebExchangeBindException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(WebExchangeBindException exception) {
        final String field = Objects.requireNonNull(exception.getFieldError()).getField();
        final ErrorResponse response =  ErrorMessage.FIELD_IS_INVALID_FOR_TRANSFER.getErrorResponse(field);

        return ResponseEntity.status(response.code().getStatus()).body(response);

    }
}

package com.lucasalmd.transfer.domain.exceptions;

import com.lucasalmd.transfer.domain.errors.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorMessage {


    FIELD_IS_INVALID_FOR_TRANSFER("Request field '%s' is invalid for transfer", HttpStatus.BAD_REQUEST),

    TRANSFER_ACCOUNTS_NOT_FOUND("Not found payer or payee accounts", HttpStatus.NOT_FOUND),

    PAYER_TYPE_IS_INVALID("Payer account is invalid for transfer", HttpStatus.UNPROCESSABLE_ENTITY),
    PAYER_BALANCE_INVALID("Payer balance is invalid for transfer", HttpStatus.UNPROCESSABLE_ENTITY),

    AUTHORIZATION_REQUEST_FAILED("Authorization request for transfer failed", HttpStatus.UNPROCESSABLE_ENTITY),
    UNAUTHORIZED_TRANSFER("Authorization was denied for transfer", HttpStatus.UNPROCESSABLE_ENTITY),

    PRODUCE_NOTIFICATION_FAILED("Produce notification for transfer failed", HttpStatus.UNPROCESSABLE_ENTITY),
    NOTIFICATION_REQUEST_FAILED("Notification request for transfer failed", HttpStatus.UNPROCESSABLE_ENTITY),
    NOTIFICATION_SEND_FAILED("Notification send for transfer failed", HttpStatus.UNPROCESSABLE_ENTITY);


    private final String message;
    private final HttpStatus status;


    public ErrorResponse getErrorResponse(Object... fields) {
        final String formattedMessage = String.format(this.message, fields);
        return new ErrorResponse(this, formattedMessage);
    }

}

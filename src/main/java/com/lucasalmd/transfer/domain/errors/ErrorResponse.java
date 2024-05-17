package com.lucasalmd.transfer.domain.errors;

import com.lucasalmd.transfer.domain.exceptions.Message;
import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorResponse(
        @Schema(description = "Code of occurred error", example = "FIELD_IS_INVALID_FOR_TRANSFER")
        Message code,
        @Schema(description = "Message of occurred error", example = "Request field 'value' is invalid for transfer")
        String message) {


}

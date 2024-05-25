package com.lucasalmd.transfer.domain.errors;

import com.lucasalmd.transfer.domain.exceptions.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorResponse(
        @Schema(description = "Code of occurred error", example = "FIELD_IS_INVALID_FOR_TRANSFER")
        ErrorMessage code,
        @Schema(description = "ErrorMessage of occurred error", example = "Request field 'value' is invalid for transfer")
        String message) {


}

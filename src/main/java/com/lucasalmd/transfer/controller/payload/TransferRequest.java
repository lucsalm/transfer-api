package com.lucasalmd.transfer.controller.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(

        @NotNull
        @Schema(description = "Payer account id", example = "1")
        Integer payer,
        @NotNull
        @Schema(description = "Payee account id", example = "2")
        Integer payee,
        @Positive
        @NotNull
        @Schema(description = "Value to be transfer", example = "1.99")
        BigDecimal value
) {
}

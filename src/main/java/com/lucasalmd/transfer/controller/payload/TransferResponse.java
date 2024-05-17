package com.lucasalmd.transfer.controller.payload;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;


public record TransferResponse(
        @Schema(description = "Id of the realized transaction", example = "1")
        Integer transactionId,
        @Schema(description = "Date of the realized transaction")
        LocalDateTime realizedAt
) { }

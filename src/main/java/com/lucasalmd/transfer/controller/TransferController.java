package com.lucasalmd.transfer.controller;

import com.lucasalmd.transfer.controller.payload.TransferRequest;
import com.lucasalmd.transfer.controller.payload.TransferResponse;
import com.lucasalmd.transfer.domain.errors.ErrorResponse;
import com.lucasalmd.transfer.providers.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
@Tag(name = "TransferController", description = "Operations related with transfers between accounts")
public class TransferController {

    private final TransferService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Execute a transfer between accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transfer successfully",
                    content = {@Content(mediaType =  MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TransferResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Request is invalid",
                    content = {@Content(mediaType =  MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Accounts not found",
                    content = {@Content(mediaType =  MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "422", description = "Operation is invalid",
                    content = {@Content(mediaType =  MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))})})
    public Mono<TransferResponse> executeTransaction(@RequestBody @Valid TransferRequest transactionRequestDTO){
        return service.executeTransaction(transactionRequestDTO);
    }
}

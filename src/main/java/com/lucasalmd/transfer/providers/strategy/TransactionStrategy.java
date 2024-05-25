package com.lucasalmd.transfer.providers.strategy;

import com.lucasalmd.transfer.domain.dtos.TransferDTO;
import com.lucasalmd.transfer.domain.enums.TypeEnum;
import com.lucasalmd.transfer.domain.models.Transfer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public interface TransactionStrategy {

    Mono<Transfer> createTransfer(TransferDTO transactionRequest);

    TypeEnum getType();


}

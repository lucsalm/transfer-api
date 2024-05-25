package com.lucasalmd.transfer.providers.strategy.impls;

import com.lucasalmd.transfer.domain.dtos.TransferDTO;
import com.lucasalmd.transfer.domain.enums.TypeEnum;
import com.lucasalmd.transfer.domain.exceptions.BusinessException;
import com.lucasalmd.transfer.domain.exceptions.ErrorMessage;
import com.lucasalmd.transfer.domain.models.Transfer;
import com.lucasalmd.transfer.providers.strategy.TransactionStrategy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.lucasalmd.transfer.domain.enums.TypeEnum.MERCHANT;

@Service
public class MerchantTransaction implements TransactionStrategy {
    public Mono<Transfer> createTransfer(TransferDTO transferDTO) {
        return Mono.error(new BusinessException(ErrorMessage.PAYER_TYPE_IS_INVALID));
    }


    public TypeEnum getType() {
        return MERCHANT;
    }
}

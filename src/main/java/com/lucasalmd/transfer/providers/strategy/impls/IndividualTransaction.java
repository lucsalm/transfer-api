package com.lucasalmd.transfer.providers.strategy.impls;

import com.lucasalmd.transfer.domain.models.Transfer;
import com.lucasalmd.transfer.domain.dtos.TransferDTO;
import com.lucasalmd.transfer.domain.entities.Account;
import com.lucasalmd.transfer.domain.entities.Transaction;
import com.lucasalmd.transfer.domain.enums.TypeEnum;
import com.lucasalmd.transfer.domain.exceptions.BusinessException;
import com.lucasalmd.transfer.domain.exceptions.Message;
import com.lucasalmd.transfer.providers.strategy.TransactionStrategy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.lucasalmd.transfer.domain.enums.TypeEnum.INDIVIDUAL;

@Service
public class IndividualTransaction implements TransactionStrategy {
    public Mono<Transfer> createTransfer(TransferDTO transferRequestDTO) {

        final BigDecimal value = transferRequestDTO.value();
        Account payer = transferRequestDTO.payer();
        Account payee = transferRequestDTO.payee();

        if (!hasBalanceToTransaction(payer.getBalance(), value))
            return Mono.error(new BusinessException(Message.PAYER_BALANCE_INVALID));

        Transaction transaction = new Transaction();

        payer.setBalance(payer.getBalance().subtract(value));
        payee.setBalance(payee.getBalance().add(value));

        transaction.setValue(value);
        transaction.setRealizedAt(LocalDateTime.now());
        transaction.setPayer(payer.getId());
        transaction.setPayee(payee.getId());

        return Mono.just(new Transfer(transaction, payer, payee));
    }

    private Boolean hasBalanceToTransaction(BigDecimal balance, BigDecimal transactionValue) {
        return balance.compareTo(transactionValue) >= 0;
    }

    public TypeEnum getType() {
        return INDIVIDUAL;
    }
}

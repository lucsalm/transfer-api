package com.lucasalmd.transfer.providers.service;

import com.lucasalmd.transfer.controller.payload.TransferRequest;
import com.lucasalmd.transfer.controller.payload.TransferResponse;
import com.lucasalmd.transfer.domain.dtos.TransferDTO;
import com.lucasalmd.transfer.domain.entities.Account;
import com.lucasalmd.transfer.domain.entities.Transaction;
import com.lucasalmd.transfer.domain.models.Transfer;
import com.lucasalmd.transfer.domain.models.Transfer.TransferBuilder;
import com.lucasalmd.transfer.domain.exceptions.BusinessException;
import com.lucasalmd.transfer.domain.exceptions.Message;
import com.lucasalmd.transfer.providers.repository.AccountRepository;
import com.lucasalmd.transfer.providers.repository.TransactionRepository;
import com.lucasalmd.transfer.providers.strategy.factory.TypeTransactionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class TransferService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TypeTransactionFactory transactionFactory;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AuthorizationService authorizationService;


    @Transactional
    public Mono<TransferResponse> executeTransaction(TransferRequest transferRequest) {
        return buildTransferDTO(transferRequest)
                .flatMap(this::createTransfer)
                .flatMap(this::authorizeTransfer)
                .flatMap(this::saveTransfer)
                .flatMap(this::produceNotification)
                .map(this::createTransferResponse);
    }

    private Mono<TransferDTO> buildTransferDTO(TransferRequest transferRequest) {
        return Mono.just(TransferDTO.builder().value(transferRequest.value()))
                .flatMap(transferDTOBuilder ->
                        this.getAccounts(transferRequest.payer(), transferRequest.payee())
                                .flatMap(accounts -> {
                                    if (accounts.size() != 2) {
                                        return Mono.error(new BusinessException(Message.TRANSFER_ACCOUNTS_NOT_FOUND));
                                    }
                                    transferDTOBuilder.payer(accounts.get(0));
                                    transferDTOBuilder.payee(accounts.get(1));
                                    return Mono.just(transferDTOBuilder);
                                })
                ).map(TransferDTO.TransferDTOBuilder::build);

    }

    private Mono<List<Account>> getAccounts(Integer payerId, Integer payeeId) {
        return accountRepository.findAllById(List.of(payerId, payeeId))
                .collectList()
                .map(accounts -> accounts.stream().distinct().toList());
    }

    private Mono<Transfer> createTransfer(TransferDTO transferDTO) {
        return transactionFactory.getStrategy(transferDTO.payer())
                .createTransfer(transferDTO);
    }

    private Mono<Transfer> authorizeTransfer(Transfer transfer) {
        return authorizationService.authorize(transfer).thenReturn(transfer);
    }

    private Mono<Transfer> saveTransfer(Transfer transfer) {
        return Mono.just(Transfer.builder())
                .flatMap(transactionBuilder ->
                        accountRepository.saveAll(List.of(transfer.payer(), transfer.payee()))
                                .collectList()
                                .doOnSuccess(accounts -> {
                                    transactionBuilder.payer(accounts.get(0));
                                    transactionBuilder.payee(accounts.get(1));
                                })
                                .then(transactionRepository.save(transfer.transaction()))
                                .doOnSuccess(transactionBuilder::transaction)
                                .thenReturn(transactionBuilder)
                ).map(TransferBuilder::build);
    }

    private Mono<Transfer> produceNotification(Transfer transfer) {
        return notificationService.produceNotification(transfer).thenReturn(transfer);
    }

    private TransferResponse createTransferResponse(Transfer transfer) {
        Transaction transaction = transfer.transaction();
        return new TransferResponse(transaction.getId(), transaction.getRealizedAt());
    }


}

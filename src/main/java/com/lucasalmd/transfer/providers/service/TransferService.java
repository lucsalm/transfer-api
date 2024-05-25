package com.lucasalmd.transfer.providers.service;

import com.lucasalmd.transfer.controller.payload.TransferRequest;
import com.lucasalmd.transfer.controller.payload.TransferResponse;
import com.lucasalmd.transfer.domain.dtos.TransferDTO;
import com.lucasalmd.transfer.domain.entities.Account;
import com.lucasalmd.transfer.domain.entities.Transaction;
import com.lucasalmd.transfer.domain.exceptions.BusinessException;
import com.lucasalmd.transfer.domain.exceptions.ErrorMessage;
import com.lucasalmd.transfer.domain.models.Transfer;
import com.lucasalmd.transfer.domain.models.Transfer.TransferBuilder;
import com.lucasalmd.transfer.providers.repository.AccountRepository;
import com.lucasalmd.transfer.providers.repository.TransactionRepository;
import com.lucasalmd.transfer.providers.strategy.factory.TypeTransactionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

    
    private final  AccountRepository accountRepository;
    
    private final  TransactionRepository transactionRepository;
    
    private final  TypeTransactionFactory transactionFactory;
    
    private final  NotificationService notificationService;
    
    private final  AuthorizationService authorizationService;


    @Transactional
    public Mono<TransferResponse> executeTransaction(TransferRequest transferRequest) {
        return buildTransferDTO(transferRequest)
                .flatMap(this::createTransfer)
                .flatMap(this::authorizeTransfer)
                .flatMap(this::saveTransfer)
                .flatMap(this::produceNotification)
                .map(this::createTransferResponse);
    }

    private final  Mono<TransferDTO> buildTransferDTO(TransferRequest transferRequest) {
        return Mono.just(TransferDTO.builder().value(transferRequest.value()))
                .flatMap(transferDTOBuilder ->
                        this.getAccounts(transferRequest.payer(), transferRequest.payee())
                                .flatMap(accounts -> {
                                    if (accounts.size() != 2) {
                                        return Mono.error(new BusinessException(ErrorMessage.TRANSFER_ACCOUNTS_NOT_FOUND));
                                    }
                                    transferDTOBuilder.payer(accounts.get(0));
                                    transferDTOBuilder.payee(accounts.get(1));
                                    return Mono.just(transferDTOBuilder);
                                })
                ).map(TransferDTO.TransferDTOBuilder::build);

    }

    private final  Mono<List<Account>> getAccounts(Integer payerId, Integer payeeId) {
        return accountRepository.findAllById(List.of(payerId, payeeId))
                .collectList()
                .map(accounts -> accounts.stream().distinct().toList());
    }

    private final  Mono<Transfer> createTransfer(TransferDTO transferDTO) {
        return transactionFactory.getStrategy(transferDTO.payer())
                .createTransfer(transferDTO);
    }

    private final  Mono<Transfer> authorizeTransfer(Transfer transfer) {
        return authorizationService.authorize(transfer).thenReturn(transfer);
    }

    private final  Mono<Transfer> saveTransfer(Transfer transfer) {
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

    private final  Mono<Transfer> produceNotification(Transfer transfer) {
        return notificationService.produceNotification(transfer).thenReturn(transfer);
    }

    private final  TransferResponse createTransferResponse(Transfer transfer) {
        Transaction transaction = transfer.transaction();
        return new TransferResponse(transaction.getId(), transaction.getRealizedAt());
    }


}

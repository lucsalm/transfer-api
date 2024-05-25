package com.lucasalmd.transfer.domain.models;

import com.lucasalmd.transfer.domain.entities.Account;
import com.lucasalmd.transfer.domain.entities.Transaction;
import lombok.Builder;

@Builder
public record Transfer(Transaction transaction, Account payer, Account payee) {
}
package com.lucasalmd.transfer.domain.dtos;

import com.lucasalmd.transfer.domain.entities.Account;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransferDTO(Account payer, Account payee, BigDecimal value) {
}

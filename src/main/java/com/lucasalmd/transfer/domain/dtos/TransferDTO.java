package com.lucasalmd.transfer.domain.dtos;

import com.lucasalmd.transfer.domain.entities.Account;
import lombok.*;

import java.math.BigDecimal;

@Builder
public record TransferDTO(Account payer, Account payee, BigDecimal value) {
}

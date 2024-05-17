package com.lucasalmd.transfer.providers.repository;

import com.lucasalmd.transfer.domain.entities.Transaction;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends R2dbcRepository<Transaction, Integer> {
}

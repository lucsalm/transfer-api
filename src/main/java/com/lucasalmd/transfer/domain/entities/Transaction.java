package com.lucasalmd.transfer.domain.entities;


import jakarta.persistence.Entity;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    private Integer id;
    private BigDecimal value;
    @Column(value = "payer_id")
    private Integer payer;
    @Column(value = "payee_id")
    private Integer payee;
    @Column(value = "realized_at")
    private LocalDateTime realizedAt;


}

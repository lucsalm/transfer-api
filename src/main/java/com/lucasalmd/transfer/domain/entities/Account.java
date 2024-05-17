package com.lucasalmd.transfer.domain.entities;

import com.lucasalmd.transfer.domain.enums.TypeEnum;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {
    @Id
    private Integer id;

    private String email;

    private BigDecimal balance;

    @Column(value = "legal_identifier")
    private String legalIdentifier;

    @Column(value = "full_name")
    private String fullName;

    @Column(value = "identifier_type")
    private TypeEnum type;

    @Column(value = "created_at")
    private LocalDateTime createdAt;

}

package com.acmebank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "transaction_Id")
    private UUID id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(columnDefinition = "TIMESTAMP", name = "transaction_time")
    private LocalDateTime transactionTime = LocalDateTime.now();

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_amount")
    private double transactionAmount;
}

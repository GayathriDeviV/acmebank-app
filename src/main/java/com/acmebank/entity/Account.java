package com.acmebank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name="account")
public class Account {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_status")
    private String accountStatus;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "account_balance")
    private double accountBalance;

    @Column(name = "currency")
    private String currency;

    @Column(columnDefinition = "TIMESTAMP", name = "created_time")
    private LocalDateTime createdTime = LocalDateTime.now();

    @Column(columnDefinition = "TIMESTAMP", name = "updated_time")
    private LocalDateTime updatedTime = LocalDateTime.now();

}

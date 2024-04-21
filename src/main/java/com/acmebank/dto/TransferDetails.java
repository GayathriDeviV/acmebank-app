package com.acmebank.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransferDetails {
        @Pattern(regexp = "[0-9]+")
        private String fromAccountNumber;
        @Pattern(regexp = "[0-9]+")
        private String toAccountNumber;
        private double transferAmount;
    }


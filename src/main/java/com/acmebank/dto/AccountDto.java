package com.acmebank.dto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AccountDto {


    private String accountNumber;

    private String accountStatus;

    private String accountType;

    private double accountBalance;

}

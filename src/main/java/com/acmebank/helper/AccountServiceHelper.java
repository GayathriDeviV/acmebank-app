package com.acmebank.helper;

import com.acmebank.dto.AccountDto;
import com.acmebank.dto.TransferDetails;
import com.acmebank.entity.Account;
import com.acmebank.entity.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AccountServiceHelper {

    public AccountDto convertToAccountEntity(Account accInfo) {
     return  AccountDto.builder()
                .accountNumber(accInfo.getAccountNumber())
                .accountBalance(accInfo.getAccountBalance())
                .accountType(accInfo.getAccountType())
                .accountStatus(accInfo.getAccountStatus())
                .build();

    }

    public Transaction createTransaction(TransferDetails transferDetails, String accountNumber, String transactionType) {

        return Transaction.builder()
                .accountNumber(accountNumber)
                .transactionAmount(transferDetails.getTransferAmount())
                .transactionType(transactionType)
                .transactionTime(LocalDateTime.now())
                .build();
    }
}

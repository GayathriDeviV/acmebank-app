package com.acmebank.service.impl;

import com.acmebank.dto.TransferDetails;
import com.acmebank.entity.Account;
import com.acmebank.entity.Transaction;
import com.acmebank.exception.NotFoundException;
import com.acmebank.helper.AccountServiceHelper;
import com.acmebank.repository.AccountRepository;
import com.acmebank.repository.TransactionRepository;
import com.acmebank.service.AccountManager;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
@Service
@Transactional
public class AccountManagerImpl implements AccountManager {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountServiceHelper accountServiceHelper;

    static Pattern pattern = Pattern.compile("[0-9.]+");

    @Override
    public ResponseEntity<Object> findByAccountNumber(String accountNumber) {

        Optional<Account> accountEntity = accountRepository.findByAccountNumber(accountNumber);
        return accountEntity.<ResponseEntity<Object>>map(account -> ResponseEntity.status(HttpStatus.FOUND).body(accountServiceHelper.convertToAccountEntity(account))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account Number " + accountNumber + " not found."));
    }

    @Override
    public ResponseEntity<Object> transferDetails(TransferDetails transferDetails, String accountNumber) {
        List<Account> accountEntities = new ArrayList<>();
        List<Transaction> transactionEntities = new ArrayList<>();
        Account fromAccount = null;
        Account toAccount = null;

        Optional<Account> fromAccountEntity = accountRepository.findByAccountNumber(transferDetails.getFromAccountNumber());
        if (fromAccountEntity.isPresent()) {
            fromAccount = fromAccountEntity.get();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transfer failed as Source Account Number is not found.");
        }


        Optional<Account> toAccountEntity = accountRepository.findByAccountNumber(transferDetails.getToAccountNumber());
        if (toAccountEntity.isPresent()) {
            toAccount = toAccountEntity.get();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transfer failed as Destination Account Number is not found.");
        }

        if(transferDetails.getTransferAmount() <=0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Transfer Amount.");
        }


        if (fromAccount.getAccountBalance() < transferDetails.getTransferAmount()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient Funds.");
        } else {
            fromAccount.setAccountBalance(fromAccount.getAccountBalance() - transferDetails.getTransferAmount());
            fromAccount.setUpdatedTime(LocalDateTime.now());
            accountEntities.add(fromAccount);

            toAccount.setAccountBalance(toAccount.getAccountBalance() + transferDetails.getTransferAmount());
            toAccount.setUpdatedTime(LocalDateTime.now());
            accountEntities.add(toAccount);

            saveAccount(accountEntities);

            Transaction fromTransaction = accountServiceHelper.createTransaction(transferDetails, fromAccount.getAccountNumber(), "DEBIT");
            transactionEntities.add(fromTransaction);

            Transaction toTransaction = accountServiceHelper.createTransaction(transferDetails, toAccount.getAccountNumber(), "CREDIT");
            transactionEntities.add(toTransaction);

            saveTransaction(transactionEntities);

            return ResponseEntity.status(HttpStatus.OK).body("Success: Amount transferred for account Number " + accountNumber);
        }

    }

    @Override
    public double getBalance(String accountNumber) {
        Optional<Account> accountEntity = accountRepository.findByAccountNumber(accountNumber);
        if (accountEntity.isPresent()) {
            return accountEntity.get().getAccountBalance();
        }
        throw new NotFoundException("Account number is not found");
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    private void saveAccount(List<Account> accountEntities) {
        accountRepository.saveAll(accountEntities);
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    private void saveTransaction(List<Transaction> transactionEntities) {
        transactionRepository.saveAll(transactionEntities);
    }
}

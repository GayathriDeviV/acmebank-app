package com.acmebank.impl;

import com.acmebank.service.impl.AccountManagerImpl;
import com.acmebank.dto.TransferDetails;
import com.acmebank.entity.Account;
import com.acmebank.helper.AccountServiceHelper;
import com.acmebank.repository.AccountRepository;
import com.acmebank.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AccountManagerImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountManagerImpl accountingService;

    @Mock
    private AccountServiceHelper accountServiceHelper;

    @Test
    public void testGetBalance() {

        String accountNumber = "123456789";
        Account mockAccount = new Account();
        mockAccount.setAccountNumber(accountNumber);
        mockAccount.setAccountBalance(1500.00);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(mockAccount));
        assertEquals(1500, accountingService.getBalance(accountNumber));
    }

    @Test
    public void testFindByExistingAccountNumber() {

        String accountNumber = "123456789";
        Account mockAccount = new Account();
        mockAccount.setAccountNumber(accountNumber);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(mockAccount));
        ResponseEntity<Object> responseEntity = accountingService.findByAccountNumber(accountNumber);
        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testFindByNonExistingAccountNumber() {
        String accountNumber = "987654321";
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());
        assertEquals(HttpStatus.NOT_FOUND,accountingService.findByAccountNumber(accountNumber).getStatusCode());
        assertEquals("Account Number " + accountNumber + " not found.",accountingService.findByAccountNumber(accountNumber).getBody());
    }

    @Test
    public void testInsufficientFundTransferDetails() {

        TransferDetails transferDetails = new TransferDetails("123456789","987654321",1000.00);

        Account mockAccount1 = new Account();
        mockAccount1.setAccountNumber("123456789");
        mockAccount1.setAccountBalance(999.00);

        Account mockAccount2 = new Account();
        mockAccount2.setAccountNumber("987654321");

        when(accountRepository.findByAccountNumber("123456789")).thenReturn(Optional.of(mockAccount1));
        when(accountRepository.findByAccountNumber("987654321")).thenReturn(Optional.of(mockAccount2));
        ResponseEntity<Object> responseEntity = accountingService.transferDetails(transferDetails, "123456789");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Insufficient Funds.", responseEntity.getBody());
    }

    @Test
    public void testFundTransferDetails() {

        TransferDetails transferDetails = new TransferDetails("123456789","987654321",1000.00);

        Account mockAccount1 = new Account();
        mockAccount1.setAccountNumber("123456789");
        mockAccount1.setAccountBalance(10000.00);

        Account mockAccount2 = new Account();
        mockAccount2.setAccountNumber("987654321");
        mockAccount2.setAccountBalance(15000.00);

        when(accountRepository.findByAccountNumber("123456789")).thenReturn(Optional.of(mockAccount1));
        when(accountRepository.findByAccountNumber("987654321")).thenReturn(Optional.of(mockAccount2));
        ResponseEntity<Object> responseEntity = accountingService.transferDetails(transferDetails, "123456789");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Success: Amount transferred for account Number 123456789", responseEntity.getBody());
    }

    @Test
    public void testInvalidSourceAccountTransferDetails() {
        TransferDetails transferDetails = new TransferDetails("123456789","987654321",15000000.00);

        Account fromAccount = new Account();
        fromAccount.setAccountNumber("123456789");
        fromAccount.setAccountBalance(1000.00);
        fromAccount.setUpdatedTime(LocalDateTime.now());

        Account toAccount = new Account();
        toAccount.setAccountNumber("987654321");
        toAccount.setAccountBalance(500.00);
        toAccount.setUpdatedTime(LocalDateTime.now());

        when(accountRepository.findByAccountNumber("89898989")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("987654321")).thenReturn(Optional.of(toAccount));
        ResponseEntity<Object> responseEntity = accountingService.transferDetails(transferDetails, "123456789");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Transfer failed as Source Account Number is not found.", responseEntity.getBody());
    }

    @Test
    public void testInvalidDestinationAccountTransferDetails() {
        TransferDetails transferDetails = new TransferDetails("123456789","987654321",15000000.00);

        Account fromAccount = new Account();
        fromAccount.setAccountNumber("123456789");
        fromAccount.setAccountBalance(1000.00);
        fromAccount.setUpdatedTime(LocalDateTime.now());

        Account toAccount = new Account();
        toAccount.setAccountNumber("987654321");
        toAccount.setAccountBalance(500.00);
        toAccount.setUpdatedTime(LocalDateTime.now());

        when(accountRepository.findByAccountNumber("123456789")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("89898989")).thenReturn(Optional.of(toAccount));
        ResponseEntity<Object> responseEntity = accountingService.transferDetails(transferDetails, "123456789");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Transfer failed as Destination Account Number is not found.", responseEntity.getBody());
    }
}

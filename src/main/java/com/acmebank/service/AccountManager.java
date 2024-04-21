package com.acmebank.service;

import com.acmebank.dto.TransferDetails;
import org.springframework.http.ResponseEntity;

public interface AccountManager {
    public ResponseEntity<Object> findByAccountNumber(String accountNumber);

    ResponseEntity<?> transferDetails(TransferDetails transferDetails, String accountNumber);

    double getBalance(String accountNumber);
}

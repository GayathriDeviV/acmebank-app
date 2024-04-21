package com.acmebank.controller;

import com.acmebank.dto.TransferDetails;
import com.acmebank.service.impl.AccountManagerImpl;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    @Autowired
    private AccountManagerImpl accountingService;

    @GetMapping(path = "/balance/{accountNumber}")
    public double getBalance(@PathVariable String accountNumber) {
        return accountingService.getBalance(accountNumber);
    }

    @PutMapping(path = "/transfer/{accNumber}")
    public ResponseEntity<?> transferDetails(@Valid @RequestBody TransferDetails transferDetails,
                                                  @PathVariable String accNumber) throws ValidationException {
        return accountingService.transferDetails(transferDetails,accNumber);
    }
}

package com.acmebank.repository;

import com.acmebank.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account,Integer> {
    Optional<Account> findByAccountNumber(String accountNumber);
}

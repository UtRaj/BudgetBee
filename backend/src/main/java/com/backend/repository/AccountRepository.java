package com.backend.repository;

import com.backend.model.Account;
import com.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    ArrayList<Account> findByUserEmail(String email);
    Account findByAccountNumber(String accountNumber);
}

package com.backend.model;

import com.backend.dto.AccountDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="accounts")
public class Account {
    @Id
    @Column(nullable = false, unique = true)
    private String accountNumber;
    @Column(nullable = false)
    private String bankName;
    @Column(nullable = false)
    private String accountType;
    @Column(nullable = false)
    private double balance;
    @Column(nullable = false)
    private String userEmail;

    public Account(AccountDTO accountDTO) {
        accountNumber = accountDTO.getAccountNumber();
        bankName = accountDTO.getBankName();
        accountType = accountDTO.getAccountType();
        balance = accountDTO.getBalance();
    }
}

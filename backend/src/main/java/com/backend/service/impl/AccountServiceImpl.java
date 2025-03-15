package com.backend.service.impl;

import com.backend.dto.AccountDTO;
import com.backend.dto.IncomeDTO;
import com.backend.exception.NotFoundException;
import com.backend.model.Account;
import com.backend.repository.AccountRepository;
import com.backend.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.backend.util.getUserEmail;

@Slf4j(topic="ACCOUNT_SERVICE")
@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    @Override
    public void addAccount(AccountDTO accountDTO) {
        Account account = new Account(accountDTO);
        account.setUserEmail(getUserEmail());
        accountRepository.save(account);
        log.info(String.format("MockAccountController %s added successfully", account.getAccountNumber()));
    }

    @Override
    public ArrayList<AccountDTO> getAccounts() {
        ArrayList<AccountDTO> accounts = new ArrayList<>();
        accountRepository.findByUserEmail(getUserEmail()).forEach(account -> accounts.add(
                new AccountDTO(
                        account.getAccountNumber(),
                        account.getBankName(),
                        account.getAccountType(),
                        account.getBalance()
                )));
        return accounts;
    }

    @Override
    public void updateAccount(AccountDTO accountDTO) {
        Account account = getAccount(accountDTO.getAccountNumber());

        account.setAccountType(accountDTO.getAccountType());
        account.setBalance(accountDTO.getBalance());
        account.setBankName(accountDTO.getBankName());

        accountRepository.save(account);
    }

    private Account getAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);

        if(account == null) {
            throw new NotFoundException("Failed to find account with account number: " + accountNumber);
        }

        return account;
    }
}

package com.backend.mock.controller;

import com.backend.dto.AccountDTO;
import com.backend.dto.IncomeDTO;
import com.backend.mock.dto.MockAccountDTO;
import com.backend.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Random;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/mock-account")
public class MockAccountController {
    private final AccountService accountService;
    private final Random random = new Random();

    private static final int MAX_RANDOM_BOUND = 5000;
    private static final int RANDOM_BOUND_SIZE = 2;
    private static final int RANDOM_TRUE_VALUE = 1;
    private static final double BALANCE_ORIGIN = -0.5;
    private static final double BALANCE_BOUND = 0.5;

    @PostMapping("")
    public ResponseEntity getAccount(@RequestBody MockAccountDTO mockAccountDTO) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber(mockAccountDTO.getAccountNumber());
        accountDTO.setBankName(mockAccountDTO.getBankName());
        accountDTO.setBalance(random.nextDouble(MAX_RANDOM_BOUND));
        accountDTO.setAccountType(random.nextInt(RANDOM_BOUND_SIZE) == RANDOM_TRUE_VALUE ? "Credit" : "Debit");
        return ResponseEntity.status(HttpStatus.OK).body(accountDTO);
    }

    @GetMapping("/balance")
    public ResponseEntity getBalance() {
        ArrayList<AccountDTO> accountList = accountService.getAccounts();
        accountList.forEach(account -> {
            account.setBalance(account.getBalance() + random.nextDouble(BALANCE_ORIGIN, BALANCE_BOUND) * account.getBalance());
            accountService.updateAccount(account);
        });
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}

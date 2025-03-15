package com.backend.controller;

import com.backend.dto.AccountDTO;
import com.backend.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    @PostMapping("")
    public ResponseEntity saveAccount(@Valid @RequestBody AccountDTO accountDTO) {
        accountService.addAccount(accountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("")
    public ResponseEntity getAccounts() {
        List<AccountDTO> accounts = accountService.getAccounts();
        return ResponseEntity.status(HttpStatus.OK).body(accounts);
    }

    @PutMapping("")
    public ResponseEntity updateAccount(@Valid @RequestBody AccountDTO accountDTO) {
        accountService.updateAccount(accountDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}

package com.backend.service.Impl;

import com.backend.dto.AccountDTO;
import com.backend.dto.IncomeDTO;
import com.backend.dto.UserDTO;
import com.backend.exception.NotFoundException;
import com.backend.model.Account;
import com.backend.repository.AccountRepository;
import com.backend.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTests {
    private AccountServiceImpl accountService;
    private AccountDTO accountDTO;
    private Account account;
    private UserDTO userDTO;

    @Mock
    private AccountRepository accountRepository;

    @Captor
    ArgumentCaptor<Account> accountArgumentCaptor;

    @BeforeEach
    public void setUp() {
        accountService = new AccountServiceImpl(accountRepository);
        accountDTO = new AccountDTO("1234567890", "RBC", "Debit", 34.07);
        account = new Account(accountDTO);
        userDTO = new UserDTO();
        userDTO.setEmail("test@gmail.com");
    }

    private void securitySetup() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn(userDTO);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testAddAccount() {
        securitySetup();
        Mockito.doReturn(account).when(accountRepository).save(accountArgumentCaptor.capture());

        accountService.addAccount(accountDTO);

        Account returnedAccount = accountArgumentCaptor.getValue();
        Mockito.verify(accountRepository, times(1)).save(returnedAccount);
        assertEquals(returnedAccount.getAccountType(), accountDTO.getAccountType());
        assertEquals(returnedAccount.getAccountNumber(), accountDTO.getAccountNumber());
        assertEquals(returnedAccount.getBankName(), accountDTO.getBankName());
        assertEquals(returnedAccount.getBalance(), accountDTO.getBalance(), 0.003);
    }

    @Test
    public void testGetAccounts() {
        securitySetup();
        List<Account> accountList = new ArrayList();
        accountList.add(account);
        Mockito.doReturn(accountList).when(accountRepository).findByUserEmail(anyString());

        List<AccountDTO> accounts = accountService.getAccounts();

        Mockito.verify(accountRepository, times(1)).findByUserEmail(anyString());
        assertEquals(accounts.size(), 1);
        AccountDTO returnedAccount = accounts.get(0);
        assertEquals(returnedAccount.getAccountType(), account.getAccountType());
        assertEquals(returnedAccount.getAccountNumber(), account.getAccountNumber());
        assertEquals(returnedAccount.getBankName(), account.getBankName());
        assertEquals(returnedAccount.getBalance(), account.getBalance(), 0.003);
    }

    @Test
    public void testUpdateAccount() {
        Mockito.doReturn(account).when(accountRepository).findByAccountNumber(accountDTO.getAccountNumber());
        accountDTO.setAccountType("Credit");
        accountDTO.setBankName("CIBC");
        Mockito.doReturn(account).when(accountRepository).save(accountArgumentCaptor.capture());

        accountService.updateAccount(accountDTO);

        Account returnedAccount = accountArgumentCaptor.getValue();
        Mockito.verify(accountRepository, times(1)).save(returnedAccount);
        assertEquals(returnedAccount.getAccountType(), accountDTO.getAccountType());
        assertEquals(returnedAccount.getAccountNumber(), accountDTO.getAccountNumber());
        assertEquals(returnedAccount.getBankName(), accountDTO.getBankName());
        assertEquals(returnedAccount.getBalance(), accountDTO.getBalance(), 0.003);
    }

    @Test
    public void testUpdateAccountNotFoundException() {
        Mockito.doReturn(null).when(accountRepository).findByAccountNumber(accountDTO.getAccountNumber());

        Assertions.assertThrows(NotFoundException.class, () ->
                accountService.updateAccount(accountDTO),
                "Failed to find account with account number: " + accountDTO.getAccountNumber());
    }
}

package com.backend.service;

import com.backend.dto.AccountDTO;
import com.backend.dto.IncomeDTO;

import java.util.ArrayList;

public interface AccountService {
    void addAccount(AccountDTO accountDTO);
    ArrayList<AccountDTO> getAccounts();
    void updateAccount(AccountDTO accountDTO);

}

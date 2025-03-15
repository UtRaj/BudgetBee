package com.backend.service;

import com.backend.dto.IncomeDTO;

import java.util.ArrayList;

public interface IncomeService {
    void addIncome(IncomeDTO incomeDTO);

    ArrayList<IncomeDTO> getAllIncomes();

    void updateIncome(Long incomeId, IncomeDTO incomeDTO);

    void deleteIncome(Long incomeId);
}

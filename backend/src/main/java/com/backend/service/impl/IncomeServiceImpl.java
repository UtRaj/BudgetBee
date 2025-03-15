package com.backend.service.impl;

import com.backend.dto.ExpenseDTO;
import com.backend.dto.IncomeDTO;
import com.backend.exception.NotFoundException;
import com.backend.model.Expense;
import com.backend.model.Income;
import com.backend.repository.IncomeRepository;
import com.backend.service.IncomeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.backend.util.getUserEmail;

@Slf4j(topic="INCOME_SERVICE")
@AllArgsConstructor
@Service
public class IncomeServiceImpl implements IncomeService {
    private final IncomeRepository incomeRepository;
    @Override
    public void addIncome(IncomeDTO incomeDTO) {
        Income income = new Income(incomeDTO);
        income.setUserEmail(getUserEmail());
        incomeRepository.save(income);
    }

    @Override
    public ArrayList<IncomeDTO> getAllIncomes() {
        ArrayList<IncomeDTO> incomes = new ArrayList<>();
        incomeRepository.findByUserEmail(getUserEmail()).forEach(income -> incomes.add(new IncomeDTO(income)));
        return incomes;
    }

    @Override
    public void updateIncome(Long incomeId, IncomeDTO incomeDTO) {
        Income income = getIncome(incomeId);
        income.setAccountNumber(incomeDTO.getAccountNumber());
        income.setAmount(incomeDTO.getAmount());
        income.setDescription(incomeDTO.getDescription());
        income.setDate(incomeDTO.getDate());
        incomeRepository.save(income);
    }

    @Override
    public void deleteIncome(Long incomeId) {
        incomeRepository.deleteById(incomeId);
    }

    private Income getIncome(Long incomeId) {
        Income income = incomeRepository.findById(incomeId).orElse(null);
        if (income == null) {
            throw new NotFoundException("Income not found with Id: " + incomeId);
        }
        return income;
    }
}

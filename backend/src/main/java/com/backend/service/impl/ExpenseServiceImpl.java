package com.backend.service.impl;

import java.util.ArrayList;

import com.backend.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.backend.dto.ExpenseDTO;
import com.backend.model.Expense;
import com.backend.repository.ExpenseRepository;
import com.backend.service.ExpenseService;

import static com.backend.util.getUserEmail;

@Slf4j(topic="EXPENSE_SERVICE")
@AllArgsConstructor
@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    @Override
    public void saveExpense(ExpenseDTO expenseDTO) {
        Expense expense = new Expense(expenseDTO);
        expense.setUserEmail(getUserEmail());
        expenseRepository.save(expense);
    }

    @Override
    public ExpenseDTO getExpenseById(Long expenseId)
    {
        return new ExpenseDTO(getExpense(expenseId));
    }

    @Override
    public ArrayList<ExpenseDTO> getAllExpenses() {
       ArrayList<ExpenseDTO> expenses = new ArrayList<>();
       expenseRepository.findByUserEmail(getUserEmail()).forEach(expense -> expenses.add(new ExpenseDTO(expense)));
       return expenses;
    }

    @Override
    public void updateExpense(Long expenseId, ExpenseDTO expenseDTO)
    {
        Expense expense = getExpense(expenseId);
        expense.setExpenseDescription(expenseDTO.getExpenseDescription());
        expense.setExpenseAmount(expenseDTO.getExpenseAmount());
        expense.setAccountNumber(expenseDTO.getAccountNumber());
        expense.setCategoryName(expenseDTO.getCategoryName());
        expense.setDate(expenseDTO.getDate());
        expenseRepository.save(expense);
    }
    
    @Override
    public void deleteExpense(Long expenseId)
    {
        expenseRepository.deleteById(expenseId);
    }

    private Expense getExpense(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId).orElse(null);
        if (expense == null) {
            throw new NotFoundException("Expense not found with Id: " + expenseId);
        }

        return expense;
    }

}

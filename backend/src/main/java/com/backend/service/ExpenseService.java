package com.backend.service;

import java.util.ArrayList;
import java.util.List;
import com.backend.dto.ExpenseDTO;

public interface ExpenseService
{
    void saveExpense(ExpenseDTO expenseDTO);

    ExpenseDTO getExpenseById(Long expenseId);

    ArrayList<ExpenseDTO> getAllExpenses();

    void updateExpense(Long expenseId, ExpenseDTO expenseDTO);

    void deleteExpense(Long expenseId);

}

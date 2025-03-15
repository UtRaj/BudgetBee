package com.backend.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.backend.model.Expense;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {

    private Long expenseId;

    private String expenseDescription;

    @NotNull
    private double expenseAmount;

    @NotBlank
    private String accountNumber;

    @NotBlank
    private Date date;

    private String categoryName;

    public ExpenseDTO(Expense expense)
    {
        expenseId = expense.getExpenseId();
        expenseDescription = expense.getExpenseDescription();
        expenseAmount = expense.getExpenseAmount();
        accountNumber = expense.getAccountNumber();
        categoryName = expense.getCategoryName();
        date = expense.getDate();
    }
}

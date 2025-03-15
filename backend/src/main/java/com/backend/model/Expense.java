package com.backend.model;

import com.backend.dto.ExpenseDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="expense")
public class Expense
{
    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    private Long expenseId;

    @Column(nullable = false)
    private double expenseAmount;

    @Column(nullable = false)
    private String accountNumber;

    @Column
    private String expenseDescription;

    @Column
    private String categoryName;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private Date date;

    public Expense(ExpenseDTO expenseDTO) {
        expenseAmount = expenseDTO.getExpenseAmount();
        accountNumber = expenseDTO.getAccountNumber();
        expenseDescription = expenseDTO.getExpenseDescription();
        categoryName = expenseDTO.getCategoryName();
        date = expenseDTO.getDate();
    }
}

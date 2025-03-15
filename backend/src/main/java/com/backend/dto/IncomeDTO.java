package com.backend.dto;

import com.backend.model.Income;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDTO {
    @NotBlank
    private Long incomeId;
    @NotBlank
    private String accountNumber;
    @NotBlank
    private double amount;
    private String description;
    @NotBlank
    private Date date;

    public IncomeDTO(Income income) {
        incomeId = income.getIncomeId();
        accountNumber = income.getAccountNumber();
        amount = income.getAmount();
        description = income.getDescription();
        date = income.getDate();
    }
}

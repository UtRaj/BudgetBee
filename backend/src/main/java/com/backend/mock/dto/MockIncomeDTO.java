package com.backend.mock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MockIncomeDTO {
    private String accountNumber;
    private double amount;
    private String description;
    private Date date;
}

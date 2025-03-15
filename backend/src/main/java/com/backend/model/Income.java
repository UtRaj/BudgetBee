package com.backend.model;

import com.backend.dto.IncomeDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GenerationType;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "income")
public class Income {
    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    private Long incomeId;
    @Column(nullable = false)
    private String accountNumber;
    @Column(nullable = false)
    private double amount;
    @Column(nullable = false)
    private String userEmail;

    private String description;
    @Column(nullable = false)
    private Date date;

    public Income(IncomeDTO incomeDTO) {
        accountNumber = incomeDTO.getAccountNumber();
        amount = incomeDTO.getAmount();
        description = incomeDTO.getDescription();
        date = incomeDTO.getDate();
    }
}

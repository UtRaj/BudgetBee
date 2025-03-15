package com.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    @NotBlank
    private String accountNumber;
    @NotBlank
    private String bankName;
    @NotBlank
    private String accountType;
    @NotBlank
    private double balance;
}

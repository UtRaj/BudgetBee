package com.backend.controller;

import com.backend.dto.ExpenseDTO;
import com.backend.dto.IncomeDTO;
import com.backend.service.IncomeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/income")
public class IncomeController {
    private final IncomeService incomeService;

    @GetMapping("/all")
    public ResponseEntity getIncomes() {
        List<IncomeDTO> incomes = incomeService.getAllIncomes();
        return ResponseEntity.status(HttpStatus.OK).body(incomes);
    }

    @PostMapping("/add")
    public ResponseEntity addIncome(@Valid @RequestBody IncomeDTO incomeDTO) {
        incomeService.addIncome(incomeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping("/{incomeId}")
    public ResponseEntity updateIncome(@PathVariable Long incomeId, @RequestBody @Valid IncomeDTO incomeDTO) {
        incomeService.updateIncome(incomeId, incomeDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("/{incomeId}")
    public ResponseEntity deleteIncome(@PathVariable Long incomeId) {
        incomeService.deleteIncome(incomeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}

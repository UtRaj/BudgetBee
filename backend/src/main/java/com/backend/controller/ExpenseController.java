package com.backend.controller;

import com.backend.dto.ExpenseDTO;
import com.backend.service.ExpenseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping("/all")
    public ResponseEntity getAllExpenses() {
        List<ExpenseDTO> expenses = expenseService.getAllExpenses();
        return ResponseEntity.status(HttpStatus.OK).body(expenses);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity getExpenseById(@PathVariable Long expenseId) {
        ExpenseDTO expense = expenseService.getExpenseById(expenseId);
        return ResponseEntity.status(HttpStatus.OK).body(expense);
    }

    @PostMapping("/add")
    public ResponseEntity addExpense(@Valid @RequestBody ExpenseDTO expenseDTO) {
        expenseService.saveExpense(expenseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping("/update/{expenseId}")
    public ResponseEntity updateExpense(@PathVariable Long expenseId, @RequestBody @Valid ExpenseDTO expenseDTO) {
        expenseService.updateExpense(expenseId, expenseDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("/delete/{expenseId}")
    public ResponseEntity deleteExpense(@PathVariable Long expenseId) {
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}

package com.backend.mock.controller;

import com.backend.dto.AccountDTO;
import com.backend.dto.ExpenseDTO;
import com.backend.dto.IncomeDTO;
import com.backend.mock.dto.MockExpenseDTO;
import com.backend.mock.dto.MockIncomeDTO;
import com.backend.service.AccountService;
import com.backend.service.ExpenseService;
import com.backend.service.IncomeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/transactions")
public class MockTransactionsController {
    private final AccountService accountService;
    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    private static final int MAX_NEW_TRANSACTION = 3;
    private static final int MAX_RANDOM_LIMIT = 50;
    private static final int ORIGIN = 1;
    private static final int BOUND = 24;
    private static final int WEEK = 7;
    private static final int SECONDS_IN_HOUR = 3600;

    @GetMapping("/expenses")
    public ResponseEntity getExpenses() {
        generateExpenses().forEach(mockExpense -> {
            ExpenseDTO expense = new ExpenseDTO();
            expense.setAccountNumber(mockExpense.getAccountNumber());
            expense.setExpenseAmount(mockExpense.getExpenseAmount());
            expense.setExpenseDescription(mockExpense.getExpenseDescription());
            expense.setDate(mockExpense.getDate());
            expenseService.saveExpense(expense);
        });
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/incomes")
    public ResponseEntity getIncomes() {
        generateIncomes().stream().forEach(mockIncome -> {
            IncomeDTO income = new IncomeDTO();
            income.setAccountNumber(mockIncome.getAccountNumber());
            income.setAmount(mockIncome.getAmount());
            income.setDescription(mockIncome.getDescription());
            income.setDate(mockIncome.getDate());
            incomeService.addIncome(income);
        });
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    private ArrayList<MockIncomeDTO> generateIncomes() {
        ArrayList<MockIncomeDTO> generatedIncomes = new ArrayList<>();
        Random random = new Random();
        int newIncomesSize = random.nextInt(MAX_NEW_TRANSACTION);
        ArrayList<String> descriptionList = getDescriptionList("/incomeDescriptionList.txt");
        int descriptionListSize = descriptionList.size();
        List<AccountDTO> accountList = accountService.getAccounts().stream().filter(account -> account.getAccountType().equals("Debit")).toList();
        int accountListSize = accountList.size();

        for (int i = 0; i < newIncomesSize; i++) {
            MockIncomeDTO income = new MockIncomeDTO();
            AccountDTO account = accountList.get(random.nextInt(accountListSize));
            String description = descriptionList.get(random.nextInt(descriptionListSize));
            double amount = account.getBalance() * random.nextDouble(ORIGIN) / MAX_RANDOM_LIMIT;
            int randomDate = SECONDS_IN_HOUR * random.nextInt(ORIGIN, BOUND) * random.nextInt(WEEK);
            Date date = new Date(System.currentTimeMillis() - randomDate);

            income.setAccountNumber(account.getAccountNumber());
            income.setAmount(amount);
            income.setDescription(description);
            income.setDate(date);
            generatedIncomes.add(income);
        }

        return generatedIncomes;
    }

    private ArrayList<MockExpenseDTO> generateExpenses() {
        ArrayList<MockExpenseDTO> generatedExpenses = new ArrayList<>();
        Random random = new Random();
        int newExpensesSize = random.nextInt(MAX_NEW_TRANSACTION);
        ArrayList<String> descriptionList = getDescriptionList("/expenseDescriptionList.txt");
        int descriptionListSize = descriptionList.size();
        ArrayList<AccountDTO> accountList = accountService.getAccounts();
        int accountListSize = accountList.size();

        if (newExpensesSize == 0) {
            return generatedExpenses;
        }

        for (int i = 0; i < newExpensesSize; i++) {
            MockExpenseDTO expense = new MockExpenseDTO();
            AccountDTO account = accountList.get(random.nextInt(accountListSize));
            String description = descriptionList.get(random.nextInt(descriptionListSize));
            double amount = account.getBalance() * random.nextDouble(ORIGIN) / MAX_RANDOM_LIMIT;
            int randomDate = SECONDS_IN_HOUR * random.nextInt(ORIGIN, BOUND) * random.nextInt(WEEK);
            Date date = new Date(System.currentTimeMillis() - randomDate);

            expense.setAccountNumber(account.getAccountNumber());
            expense.setExpenseAmount(amount);
            expense.setExpenseDescription(description);
            expense.setDate(date);
            generatedExpenses.add(expense);
        }

        return generatedExpenses;
    }

    private ArrayList<String> getDescriptionList(String path) {
        ArrayList<String> descriptionList = new ArrayList<>();
        InputStream in = getClass().getResourceAsStream(path);
        Scanner listReader = new Scanner(in);
        while (listReader.hasNextLine()) {
            descriptionList.add(listReader.nextLine());
        }
        return descriptionList;
    }
}

package com.backend.service.Impl;

import com.backend.dto.ExpenseDTO;
import com.backend.dto.UserDTO;
import com.backend.exception.NotFoundException;
import com.backend.model.Expense;
import com.backend.repository.ExpenseRepository;
import com.backend.service.impl.ExpenseServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceImplTests
{
    private ExpenseServiceImpl expenseService;

    private ExpenseDTO expenseDTO;

    private Expense expense;

    private UserDTO userDTO;

    @Mock
    private ExpenseRepository expenseRepository;

    @Captor
    ArgumentCaptor<Expense> expenseArgumentCaptor;

    @BeforeEach
    public void setUp()
    {
        expenseService = new ExpenseServiceImpl(expenseRepository);
        expenseDTO = new ExpenseDTO(1311200L,"home rent",390.70,"1234567890",new Date(),"some category");
        expense = new Expense(expenseDTO);
        userDTO = new UserDTO();
        userDTO.setEmail("test@gmail.com");

    }

    private void securitySetup()
    {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn(userDTO);
        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    void testSaveExpense()
    {
        securitySetup();
        Mockito.doReturn(expense).when(expenseRepository).save(expenseArgumentCaptor.capture());

        expenseService.saveExpense(expenseDTO);

        Expense returnedExpense = expenseArgumentCaptor.getValue();
        Mockito.verify(expenseRepository, times(1)).save(returnedExpense);
        assertEquals(returnedExpense.getAccountNumber(), expenseDTO.getAccountNumber());
        assertEquals(returnedExpense.getDate(), expenseDTO.getDate());
        assertEquals(returnedExpense.getExpenseDescription(), expenseDTO.getExpenseDescription());
        assertEquals(returnedExpense.getExpenseAmount(), expenseDTO.getExpenseAmount(), 0.003);
        assertEquals(returnedExpense.getCategoryName(), expenseDTO.getCategoryName());

    }

    @Test
    public void testGetExpenseById()
    {
        Long expenseId = 1311200L;
        Mockito.doReturn(Optional.of(expense)).when(expenseRepository).findById(expenseId);

        ExpenseDTO returnedExpense = expenseService.getExpenseById(expenseId);

        Mockito.verify(expenseRepository, times(1)).findById(expenseId);
        assertEquals(returnedExpense.getDate(), expense.getDate());
        assertEquals(returnedExpense.getAccountNumber(), expense.getAccountNumber());
        assertEquals(returnedExpense.getExpenseDescription(), expense.getExpenseDescription());
        assertEquals(returnedExpense.getExpenseAmount(), expense.getExpenseAmount(), 0.003);
        assertEquals(returnedExpense.getCategoryName(), expense.getCategoryName());

    }

    @Test
    public void testGetExpenseByIdNotFoundException()
    {
        Long nonExistentExpenseId = 999L;

        Mockito.when(expenseRepository.findById(nonExistentExpenseId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> expenseService.getExpenseById(nonExistentExpenseId));
    }

    @Test
    public void testGetAllExpenses()
    {
        securitySetup();
        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        Mockito.doReturn(expenseList).when(expenseRepository).findByUserEmail(anyString());

        ArrayList<ExpenseDTO> expenses = expenseService.getAllExpenses();

        Mockito.verify(expenseRepository, times(1)).findByUserEmail(anyString());
        assertEquals(expenses.size(), 1);
        ExpenseDTO returnedExpense = expenses.get(0);
        assertEquals(returnedExpense.getDate(), expense.getDate());
        assertEquals(returnedExpense.getAccountNumber(), expense.getAccountNumber());
        assertEquals(returnedExpense.getExpenseDescription(), expense.getExpenseDescription());
        assertEquals(returnedExpense.getExpenseAmount(), expense.getExpenseAmount(), 0.003);
        assertEquals(returnedExpense.getCategoryName(), expense.getCategoryName());

    }

    @Test
    public void testUpdateExpense()
    {
        Mockito.doReturn(Optional.of(expense)).when(expenseRepository).findById(expenseDTO.getExpenseId());
        expenseDTO.setExpenseAmount(500.01);
        expenseDTO.setExpenseDescription("updated expense description");
        Mockito.doReturn(expense).when(expenseRepository).save(expenseArgumentCaptor.capture());

        expenseService.updateExpense(expenseDTO.getExpenseId(), expenseDTO);

        Expense returnedExpense = expenseArgumentCaptor.getValue();
        Mockito.verify(expenseRepository, times(1)).save(returnedExpense);
        assertEquals(returnedExpense.getAccountNumber(), expenseDTO.getAccountNumber());
        assertEquals(returnedExpense.getDate(), expenseDTO.getDate());
        assertEquals(returnedExpense.getExpenseDescription(), expenseDTO.getExpenseDescription());
        assertEquals(returnedExpense.getExpenseAmount(), expenseDTO.getExpenseAmount(), 0.003);
        assertEquals(returnedExpense.getCategoryName(), expenseDTO.getCategoryName());

    }

    @Test
    public void testUpdateExpenseNotFoundException()
    {
        Mockito.doReturn(Optional.empty()).when(expenseRepository).findById(expenseDTO.getExpenseId());

        assertThrows(NotFoundException.class, () ->
                        expenseService.updateExpense(expenseDTO.getExpenseId(),expenseDTO),
                "Income not found with Id: " + expenseDTO.getExpenseId());

    }

    @Test
    public void testDeleteExpense()
    {

        Mockito.doNothing().when(expenseRepository).deleteById(expenseDTO.getExpenseId());

        expenseService.deleteExpense(expenseDTO.getExpenseId());

        Mockito.verify(expenseRepository, times(1)).deleteById(expenseDTO.getExpenseId());

    }

}

package com.backend.service.Impl;

import com.backend.dto.IncomeDTO;
import com.backend.dto.UserDTO;
import com.backend.exception.NotFoundException;
import com.backend.model.Income;
import com.backend.repository.IncomeRepository;
import com.backend.service.impl.IncomeServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class IncomeServiceImplTests {
    private IncomeServiceImpl incomeService;
    private IncomeDTO incomeDTO;
    private Income income;
    private UserDTO userDTO;

    @Mock
    private IncomeRepository incomeRepository;

    @Captor
    ArgumentCaptor<Income> incomeArgumentCaptor;

    @BeforeEach
    public void setUp() {
        incomeService = new IncomeServiceImpl(incomeRepository);
        incomeDTO = new IncomeDTO(4567654L, "1234567890", 34.07, "my first income", new Date());
        income = new Income(incomeDTO);
        userDTO = new UserDTO();
        userDTO.setEmail("test@gmail.com");
    }

    private void securitySetup() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn(userDTO);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testAddIncome() {
        securitySetup();
        Mockito.doReturn(income).when(incomeRepository).save(incomeArgumentCaptor.capture());

        incomeService.addIncome(incomeDTO);

        Income returnedIncome = incomeArgumentCaptor.getValue();
        Mockito.verify(incomeRepository, times(1)).save(returnedIncome);
        assertEquals(returnedIncome.getAccountNumber(), incomeDTO.getAccountNumber());
        assertEquals(returnedIncome.getDate(), incomeDTO.getDate());
        assertEquals(returnedIncome.getDescription(), incomeDTO.getDescription());
        assertEquals(returnedIncome.getAmount(), incomeDTO.getAmount(), 0.003);
    }

    @Test
    public void testGetIncomes() {
        securitySetup();
        ArrayList<Income> incomeList = new ArrayList<>();
        incomeList.add(income);
        Mockito.doReturn(incomeList).when(incomeRepository).findByUserEmail(anyString());

        ArrayList<IncomeDTO> incomes = incomeService.getAllIncomes();

        Mockito.verify(incomeRepository, times(1)).findByUserEmail(anyString());
        assertEquals(incomes.size(), 1);
        IncomeDTO returnedIncome = incomes.get(0);
        assertEquals(returnedIncome.getDate(), income.getDate());
        assertEquals(returnedIncome.getAccountNumber(), income.getAccountNumber());
        assertEquals(returnedIncome.getDescription(), income.getDescription());
        assertEquals(returnedIncome.getAmount(), income.getAmount(), 0.003);
    }

    @Test
    public void testUpdateIncome() {
        Mockito.doReturn(Optional.of(income)).when(incomeRepository).findById(incomeDTO.getIncomeId());
        incomeDTO.setAmount(50.01);
        incomeDTO.setDescription("updated income description");
        Mockito.doReturn(income).when(incomeRepository).save(incomeArgumentCaptor.capture());

        incomeService.updateIncome(incomeDTO.getIncomeId(), incomeDTO);

        Income returnedIncome = incomeArgumentCaptor.getValue();
        Mockito.verify(incomeRepository, times(1)).save(returnedIncome);
        assertEquals(returnedIncome.getDate(), incomeDTO.getDate());
        assertEquals(returnedIncome.getAccountNumber(), incomeDTO.getAccountNumber());
        assertEquals(returnedIncome.getDescription(), incomeDTO.getDescription());
        assertEquals(returnedIncome.getAmount(), incomeDTO.getAmount(), 0.003);
    }

    @Test
    public void testUpdateIncomeNotFoundException() {
        Mockito.doReturn(Optional.empty()).when(incomeRepository).findById(incomeDTO.getIncomeId());

        Assertions.assertThrows(NotFoundException.class, () ->
                        incomeService.updateIncome(incomeDTO.getIncomeId(), incomeDTO),
                "Income not found with Id: " + incomeDTO.getIncomeId());
    }

    @Test
    public void testDeleteIncome() {
        Mockito.doNothing().when(incomeRepository).deleteById(incomeDTO.getIncomeId());

        incomeService.deleteIncome(incomeDTO.getIncomeId());

        Mockito.verify(incomeRepository, times(1)).deleteById(incomeDTO.getIncomeId());
    }
}

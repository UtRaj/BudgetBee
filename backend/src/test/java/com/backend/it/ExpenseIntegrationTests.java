package com.backend.it;

import com.backend.BackendApplication;
import com.backend.dto.ExpenseDTO;
import com.backend.dto.UserDTO;
import com.backend.model.JwtRequest;
import com.backend.service.ExpenseService;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExpenseIntegrationTests
{
    @LocalServerPort
    private int port;

    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    @Autowired
    private ExpenseService expenseService;

    private String jwtToken;
    private UserDTO userDTO;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    public void setUp() throws Exception
    {

        //call the /authenticate endpoint to retrieve a JWT token
        JwtRequest authenticationRequest = new JwtRequest();
        authenticationRequest.setEmail("testing@gmail.com");
        authenticationRequest.setPassword("Testing@123");

        HttpEntity<JwtRequest> authenticationRequestEntity = new HttpEntity<>(authenticationRequest,headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/authenticate"), HttpMethod.POST,authenticationRequestEntity, String.class);
        jwtToken = response.getBody().substring(13,response.getBody().length()-2);

        userDTO = new UserDTO();
        userDTO.setEmail("testing@gmail.com");
        userDTO.setPassword("Testing@123");
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn(userDTO);
        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    public void testAddExpense() throws JSONException
    {
        ExpenseDTO expenseDTO = new ExpenseDTO(null, "Testing expenseDesc new", 234.567, "1111111111", new Date(), "Salary");
        headers.set("Authorization", AUTH_HEADER_PREFIX + jwtToken);
        HttpEntity<ExpenseDTO> requestEntity = new HttpEntity<>(expenseDTO, headers);
        ResponseEntity<ExpenseDTO> responseEntity = restTemplate.exchange(createURLWithPort("/expense/add"), HttpMethod.POST, requestEntity, ExpenseDTO.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        List<ExpenseDTO> response = new ArrayList<>();
        response = expenseService.getAllExpenses();

        Boolean flag = false;
        for (ExpenseDTO e : response) {
            if(e.getExpenseDescription().equals("Testing expenseDesc new") && e.getExpenseAmount() == 234.567 && e.getAccountNumber().equals("1111111111")) {
                flag = true;
                break;
            }
        }
        assertEquals(true, flag);
    }

    @Test
    public void testGetAllExpenses() throws JSONException
    {

        headers.set("Authorization", AUTH_HEADER_PREFIX + jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(createURLWithPort("/expense/all"), HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

    }

    @Test
    public void testGetExpenseByID() throws JSONException
    {
        Long expenseIdForGet = null;
        ExpenseDTO expenseDTOForAdd = new ExpenseDTO(null, "test for get", 234.567, "1111111111", new Date(), "Salary");
        expenseService.saveExpense(expenseDTOForAdd);

        List<ExpenseDTO> response = new ArrayList<>();
        response = expenseService.getAllExpenses();
        for (ExpenseDTO e : response) {
            if(e.getExpenseDescription().equals("test for get")) {
                expenseIdForGet = e.getExpenseId();
                break;
            }
        }

        headers.set("Authorization", AUTH_HEADER_PREFIX + jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(createURLWithPort("/expense/" + expenseIdForGet), HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

    }

    @Test
    public void testUpdateExpense() throws JSONException
    {

        Long expenseIdForUpdate = null;
        ExpenseDTO expenseDTOForAdd = new ExpenseDTO(null, "test for update", 234.567, "1111111111", new Date(), "Salary");
        expenseService.saveExpense(expenseDTOForAdd);

        List<ExpenseDTO> response = new ArrayList<>();
        response = expenseService.getAllExpenses();
        for (ExpenseDTO e : response) {
            if(e.getExpenseDescription().equals("test for update")) {
                expenseIdForUpdate = e.getExpenseId();
                break;
            }
        }

        ExpenseDTO expenseDTOForUpdate = new ExpenseDTO(null, "123 Testing expenseDesc new", 111.567, "1111111111", new Date(), "Salary");
        headers.set("Authorization", AUTH_HEADER_PREFIX + jwtToken);
        HttpEntity<ExpenseDTO> requestEntity = new HttpEntity<>(expenseDTOForUpdate, headers);

        ResponseEntity<ExpenseDTO> responseEntity = restTemplate.exchange(createURLWithPort("/expense/update/" + expenseIdForUpdate), HttpMethod.PUT, requestEntity, ExpenseDTO.class);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        List<ExpenseDTO> response2 = new ArrayList<>();
        response2 = expenseService.getAllExpenses();
        Boolean flag = false;
        for (ExpenseDTO e : response2) {
            if(e.getExpenseId().longValue() == expenseIdForUpdate && e.getExpenseDescription().equals("123 Testing expenseDesc new") && e.getExpenseAmount() == 111.567) {
                flag = true;
                break;
            }
        }
        assertEquals(true, flag);

    }

    @Test
    public void testDeleteExpense() throws JSONException
    {
        Long expenseIdForDelete = null;
        ExpenseDTO expenseDTOForDelete = new ExpenseDTO(null, "test for update", 234.567, "1111111111", new Date(), "Salary");
        expenseService.saveExpense(expenseDTOForDelete);

        List<ExpenseDTO> response = new ArrayList<>();
        response = expenseService.getAllExpenses();
        for (ExpenseDTO e : response) {
            if(e.getExpenseDescription().equals("test for update")) {
                expenseIdForDelete = e.getExpenseId();
                break;
            }
        }
        headers.set("Authorization", AUTH_HEADER_PREFIX + jwtToken);
        HttpEntity<ExpenseDTO> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<ExpenseDTO> responseEntity = restTemplate.exchange(createURLWithPort("/expense/delete/" + expenseIdForDelete), HttpMethod.DELETE, requestEntity, ExpenseDTO.class);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        List<ExpenseDTO> response2 = new ArrayList<>();
        response2 = expenseService.getAllExpenses();

        Boolean flag = false;
        for (ExpenseDTO e : response2) {
            if(e.getExpenseId().longValue() == expenseIdForDelete && e.getExpenseDescription().equals("123 Testing expenseDesc new") && e.getExpenseAmount() == 111.567) {
                flag = true;
                break;
            }
        }
        assertEquals(false, flag);
    }


    @AfterEach
    public void cleanUp() {
        List<ExpenseDTO> response = expenseService.getAllExpenses();

        for (ExpenseDTO e : response) {
                expenseService.deleteExpense(e.getExpenseId());
        }
    }

    private String createURLWithPort(String url)
    {
        return "http://localhost:"+port+url;
    }

}

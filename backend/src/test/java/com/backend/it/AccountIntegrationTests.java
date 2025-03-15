package com.backend.it;

import com.backend.BackendApplication;
import com.backend.dto.AccountDTO;
import com.backend.dto.UserDTO;
import com.backend.model.Account;
import com.backend.model.JwtRequest;
import com.backend.repository.AccountRepository;
import com.backend.repository.UserRepository;
import com.backend.service.AccountService;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountIntegrationTests
{
    @LocalServerPort
    private int port;

    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;

    private String jwtToken;
    private UserDTO userDTO;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    public void setUp() throws Exception
        {

            //call the /authenticate endpoint to retrieve a JWT token
            JwtRequest authenticationRequest = new JwtRequest();
            authenticationRequest.setEmail("integrationtesting@gmail.com");
            authenticationRequest.setPassword("Testing@123");

            HttpEntity<JwtRequest> authenticationRequestEntity = new HttpEntity<>(authenticationRequest,headers);
            ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/authenticate"), HttpMethod.POST,authenticationRequestEntity, String.class);
            jwtToken = response.getBody().substring(13,response.getBody().length()-2);

            userDTO = new UserDTO();
            userDTO.setEmail("integrationtesting@gmail.com");
            userDTO.setPassword("Testing@123");

            Authentication authentication = Mockito.mock(Authentication.class);
            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
            Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn(userDTO);
            SecurityContextHolder.setContext(securityContext);

        }

    @Test
    public void testSaveAccount() throws JSONException
    {
        AccountDTO accountDTO = new AccountDTO("1234567890", "Bank A", "Checking", 5000.0);
        headers.set("Authorization", AUTH_HEADER_PREFIX + jwtToken);

        HttpEntity<AccountDTO> requestEntity = new HttpEntity<>(accountDTO, headers);
        ResponseEntity<AccountDTO> responseEntity = restTemplate.exchange(createURLWithPort("/account"), HttpMethod.POST, requestEntity, AccountDTO.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
        Optional<Account> account = Optional.ofNullable(accountRepository.findByAccountNumber(accountDTO.getAccountNumber()));
        Assertions.assertTrue(account.isPresent());
    }

    @Test
    public void testGetAccounts()
    {
        AccountDTO accountDTO1 = new AccountDTO("1234567890", "Bank A", "Checking", 5000.0);
        AccountDTO accountDTO2 = new AccountDTO("0987654321", "Bank B", "Savings", 10000.0);
        accountService.addAccount(accountDTO1);
        accountService.addAccount(accountDTO2);

        headers.set("Authorization", AUTH_HEADER_PREFIX + jwtToken);
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<List> responseEntity = restTemplate.exchange(createURLWithPort("/account"), HttpMethod.GET, requestEntity, List.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().size());
        Assertions.assertTrue(responseEntity.getBody().toString().contains(accountDTO1.getAccountNumber()));
        Assertions.assertTrue(responseEntity.getBody().toString().contains(accountDTO2.getAccountNumber()));
    }

    @Test
    public void testUpdateAccount()
    {
        // Create a new account
        AccountDTO accountDTO = new AccountDTO("1234567890", "Bank A", "Checking", 5000.0);
        accountService.addAccount(accountDTO);

        // Update the account
        accountDTO.setBalance(6000.0);
        headers.set("Authorization", AUTH_HEADER_PREFIX + jwtToken);
        headers.set("Content-Type", "application/json");
        HttpEntity<AccountDTO> requestEntity = new HttpEntity<>(accountDTO, headers);
        ResponseEntity<AccountDTO> responseEntity = restTemplate.exchange(createURLWithPort("/account"), HttpMethod.PUT, requestEntity, AccountDTO.class);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        // Verify the account has been updated
        Optional<Account> account = Optional.ofNullable(accountRepository.findByAccountNumber(accountDTO.getAccountNumber()));
        Assertions.assertTrue(account.isPresent());
        assertEquals(accountDTO.getBalance(), account.get().getBalance());
    }

    private String createURLWithPort(String url)
    {
        return "http://localhost:"+port+url;
    }

    @AfterEach
    public void cleanUp(){
        accountRepository.findByUserEmail("integrationtesting@gmail.com").forEach(account -> accountRepository.delete(account));
    }

}

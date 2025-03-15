package com.backend.it;

import com.backend.BackendApplication;
import com.backend.dto.IncomeDTO;
import com.backend.dto.UserDTO;
import com.backend.model.Income;
import com.backend.model.JwtRequest;
import com.backend.repository.IncomeRepository;
import com.backend.service.IncomeService;
import org.junit.jupiter.api.AfterEach;
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


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IncomeIntegrationTests
{
    @LocalServerPort
    private int port;
    private String jwtToken;
    @Autowired
    private IncomeRepository incomeRepository;
    @Autowired
    private IncomeService incomeService;
    private UserDTO userDTO;
    private static final String AUTH_HEADER_PREFIX = "Bearer ";

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
    public void testGetIncomes()
    {
        headers.set("Authorization", AUTH_HEADER_PREFIX + jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(createURLWithPort("/income/all"), HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
    @Test
    public void testAddIncome() throws NoSuchFieldException
    {
        IncomeDTO incomeDTO = new IncomeDTO(123L,"995246789",15000.89,"Salary", new Date());
        headers.set("Authorization", "Bearer "+ jwtToken);
        HttpEntity<IncomeDTO> requestEntity = new HttpEntity<>(incomeDTO, headers);
        ResponseEntity<IncomeDTO> responseEntity = restTemplate.exchange(createURLWithPort("/income/add"), HttpMethod.POST, requestEntity, IncomeDTO.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        Optional<Optional<Income>> income = Optional.ofNullable(incomeRepository.findById(incomeDTO.getIncomeId()));
        assertTrue(income.isPresent());

    }

    @Test
    public void testUpdateIncome()
    {
        Long incomeIdForUpdate = null;
        IncomeDTO incomeDTO = new IncomeDTO(null,"99874597",123089.12,"test salary",new Date());
        incomeService.addIncome(incomeDTO);

        List<IncomeDTO> response = new ArrayList<>();
        response = incomeService.getAllIncomes();
        for(IncomeDTO income: response)
        {
            if(income.getDescription().equals("test salary"))
            {
                incomeIdForUpdate = income.getIncomeId();
                break;
            }
        }

        IncomeDTO incomeDTOForUpdate = new IncomeDTO(null,"99874597",1667.12,"testing the updation of salary",new Date());
        headers.set("Authorization", AUTH_HEADER_PREFIX + jwtToken);
        HttpEntity<IncomeDTO> requestEntity = new HttpEntity<>(incomeDTOForUpdate,headers);

        ResponseEntity<IncomeDTO> responseEntity = restTemplate.exchange(createURLWithPort("/income/"+incomeIdForUpdate),HttpMethod.PUT,requestEntity,IncomeDTO.class);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        List<IncomeDTO> expectedResponse = new ArrayList<>();
        expectedResponse = incomeService.getAllIncomes();
        Boolean isUpdated = false;
        for(IncomeDTO income: expectedResponse){
            if(income.getIncomeId().longValue() == incomeIdForUpdate && income.getDescription().equals("testing the updation of salary") && income.getAmount() == 1667.12){
                isUpdated = true;
                break;
            }
        }
        assertEquals(true,isUpdated);
    }

    @Test
    public void testDeleteIncome()
    {
        Long incomeIdForDelete = null;
        IncomeDTO incomeDTO = new IncomeDTO(null,"99874567",1230089.12,"test salary",new Date());
        incomeService.addIncome(incomeDTO);

        List<IncomeDTO> response = new ArrayList<>();
        response = incomeService.getAllIncomes();
        for(IncomeDTO income: response)
        {
            if(income.getDescription().equals("test salary"))
            {
                incomeIdForDelete = income.getIncomeId();
                break;
            }
        }
        headers.set("Authorization", AUTH_HEADER_PREFIX + jwtToken);
        HttpEntity<IncomeDTO> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<IncomeDTO> responseEntity = restTemplate.exchange(createURLWithPort("/income/"+incomeIdForDelete),HttpMethod.DELETE,requestEntity,IncomeDTO.class);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        List<IncomeDTO> expectedResponse = new ArrayList<>();
        Boolean isDeleted = false;
        for(IncomeDTO income: expectedResponse)
        {
            if(income.getIncomeId().longValue() == incomeIdForDelete && income.getDescription().equals("test salary"))
            {
                isDeleted = true;
                break;
            }
        }
        assertEquals(false,isDeleted);
    }

    private String createURLWithPort(String url)
    {
        return "http://localhost:"+port+url;
    }

    @AfterEach
    public void cleanUp(){
        incomeRepository.findByUserEmail("integrationtesting@gmail.com").forEach(income -> incomeRepository.delete(income));
    }

}

package com.backend.it;

import com.backend.BackendApplication;
import com.backend.dto.CategoryDTO;
import com.backend.dto.UserDTO;
import com.backend.model.JwtRequest;
import com.backend.repository.CategoryRepository;
import com.backend.service.CategoryService;
import org.json.JSONException;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryIntegrationTests
{
    @LocalServerPort
    private int port;

    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

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
    public void testAddCategory() throws JSONException
    {
        CategoryDTO categoryDTO = new CategoryDTO(null, "test category", "testing@gmail.com");
        headers.set("Authorization", AUTH_HEADER_PREFIX + jwtToken);
        HttpEntity<CategoryDTO> requestEntity = new HttpEntity<>(categoryDTO, headers);
        ResponseEntity<CategoryDTO> responseEntity = restTemplate.exchange(createURLWithPort("/category/add"), HttpMethod.POST, requestEntity, CategoryDTO.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        List<CategoryDTO> response = new ArrayList<>();
        response = categoryService.getAllCategories();

        Boolean flag = false;
        for (CategoryDTO c : response) {
            if(c.getCategoryName().equals("test category")) {
                flag = true;
                break;
            }
        }
        assertEquals(true, flag);
    }

    @Test
    public void testGetAllCategories() throws JSONException
    {

        headers.set("Authorization", AUTH_HEADER_PREFIX + jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(createURLWithPort("/category/all"), HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

    }

    @AfterEach
    public void cleanUp() {
        categoryRepository.findByUserEmail("testing@gmail.com").forEach(category -> categoryRepository.delete(category));
    }

    private String createURLWithPort(String url)
    {
        return "http://localhost:"+port+url;
    }

}

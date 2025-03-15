package com.backend.service.Impl;

import com.backend.dto.CategoryDTO;
import com.backend.dto.UserDTO;
import com.backend.model.Category;
import com.backend.repository.CategoryRepository;
import com.backend.service.impl.CategoryServiceImpl;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTests
{
    private CategoryServiceImpl categoryService;

    private CategoryDTO categoryDTO;

    private Category category;

    private UserDTO userDTO;

    @Mock
    private CategoryRepository categoryRepository;

    @Captor
    ArgumentCaptor<Category> categoryArgumentCaptor;

    @BeforeEach
    public void setUp()
    {
        categoryService = new CategoryServiceImpl(categoryRepository);
        categoryDTO = new CategoryDTO(558901L,"rental","test@gmail.com");
        category = new Category(categoryDTO);
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
    public void testAddCategory()
    {
        securitySetup();
        Mockito.doReturn(category).when(categoryRepository).save(categoryArgumentCaptor.capture());

        categoryService.addCategory(categoryDTO);

        Category returnedCategory = categoryArgumentCaptor.getValue();
        Mockito.verify(categoryRepository, times(1)).save(returnedCategory);
        assertEquals(returnedCategory.getCategoryId(), categoryDTO.getCategoryId());
        assertEquals(returnedCategory.getCategoryName(), categoryDTO.getCategoryName());
        assertEquals(returnedCategory.getUserEmail(), categoryDTO.getUserEmail());

    }

    @Test
    public void testGetAllCategories()
    {
        securitySetup();
        ArrayList<Category> categoryList = new ArrayList<>();
        categoryList.add(category);
        Mockito.doReturn(categoryList).when(categoryRepository).findByUserEmail(anyString());

        ArrayList<CategoryDTO> categories = categoryService.getAllCategories();

        Mockito.verify(categoryRepository, times(1)).findByUserEmail(anyString());
        assertEquals(categories.size(), 1);
        CategoryDTO returnedCategory = categories.get(0);
        assertEquals(returnedCategory.getCategoryId(), category.getCategoryId());
        assertEquals(returnedCategory.getCategoryName(), category.getCategoryName());
        assertEquals(returnedCategory.getUserEmail(), category.getUserEmail());

    }

}

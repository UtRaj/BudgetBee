package com.backend.service.impl;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.backend.dto.CategoryDTO;
import com.backend.model.Category;
import com.backend.repository.CategoryRepository;
import com.backend.service.CategoryService;
import static com.backend.util.getUserEmail;

@Slf4j(topic = "CATEGORY_SERVICE")
@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService
{
    private final CategoryRepository categoryRepository;
    @Override
    public void addCategory(CategoryDTO categoryDTO)
    {
        Category category = new Category(categoryDTO);
        category.setUserEmail(getUserEmail());
        categoryRepository.save(category);
    }

    @Override
    public ArrayList<CategoryDTO> getAllCategories()
    {
        ArrayList<CategoryDTO> categories = new ArrayList<>();
        categoryRepository.findByUserEmail(getUserEmail()).forEach(category -> categories.add(new CategoryDTO(category)));
        return categories;
    }
}


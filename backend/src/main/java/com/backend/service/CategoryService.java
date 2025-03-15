package com.backend.service;

import java.util.ArrayList;

import com.backend.dto.CategoryDTO;
import com.backend.exception.AlreadyExistsException;
import com.backend.exception.BadRequestException;
import com.backend.exception.NotFoundException;

public interface CategoryService
{
    void addCategory(CategoryDTO categoryDTO);
    ArrayList<CategoryDTO> getAllCategories();
}

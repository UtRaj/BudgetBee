package com.backend.controller;

import com.backend.dto.CategoryDTO;
import com.backend.service.CategoryService;
import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/category")
public class CategoryController
{
    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity getAllCategories()
    {
        List<CategoryDTO> categoryDTOList = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(categoryDTOList);
    }

    @PostMapping("/add")
    public ResponseEntity addCategory(@Valid @RequestBody CategoryDTO categoryDTO)
    {
        categoryService.addCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}

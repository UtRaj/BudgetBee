package com.backend.dto;

import javax.validation.constraints.NotBlank;
import com.backend.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO
{
    private Long categoryId;
    @NotBlank
    private String categoryName;
    private String userEmail;
    public CategoryDTO(Category category)
    {
        categoryId=category.getCategoryId();
        categoryName=category.getCategoryName();
        userEmail=category.getUserEmail();
    }

}

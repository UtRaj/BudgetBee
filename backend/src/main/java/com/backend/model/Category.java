package com.backend.model;

import com.backend.dto.CategoryDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category
{
    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    private Long categoryId;
    @Column(nullable = false)
    private String categoryName;
    @Column(nullable = false)
    private String userEmail;
    public Category(CategoryDTO categoryDTO)
    {
        categoryId=categoryDTO.getCategoryId();
        categoryName=categoryDTO.getCategoryName();
        userEmail=categoryDTO.getUserEmail();
    }

}


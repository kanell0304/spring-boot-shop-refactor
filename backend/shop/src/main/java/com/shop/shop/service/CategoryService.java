package com.shop.shop.service;

import com.shop.shop.domain.category.Category;
import com.shop.shop.dto.CategoryDTO;
import com.shop.shop.dto.ItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    CategoryDTO registerCategory(CategoryDTO categoryDTO);

    CategoryDTO editCategory(CategoryDTO categoryDTO);

    void deleteCategory(Long id);

    public Page<Category> getAllCategory(Pageable pageable);

    public Page<Category> getAllItemsFromCategory(Pageable pageable, Long categoryId);

}

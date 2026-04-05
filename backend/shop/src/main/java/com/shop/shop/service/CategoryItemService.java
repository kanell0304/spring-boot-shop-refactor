package com.shop.shop.service;

import com.shop.shop.domain.category.Category;
import com.shop.shop.domain.category.CategoryItem;
import com.shop.shop.domain.item.Item;
import com.shop.shop.dto.CategoryDTO;
import com.shop.shop.dto.CategoryItemDTO;
import com.shop.shop.dto.ItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryItemService {

    CategoryItemDTO registerCategoryItem(Item item, Long categoryId);

    public Page<CategoryItemDTO> getAllCategoryItem(Pageable pageable);

    public Page<CategoryItemDTO> getAllItemsFromCategoryItem(Pageable pageable, Long categoryId);

    public Page<CategoryItemDTO> getAllItemsFromCategoryItemWithStatus(Pageable pageable, Long categoryId,
            String categoryItemStatus);

}

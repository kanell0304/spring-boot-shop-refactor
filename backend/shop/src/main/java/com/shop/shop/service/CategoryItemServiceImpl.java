package com.shop.shop.service;

import com.shop.shop.domain.category.Category;
import com.shop.shop.domain.category.CategoryItem;
import com.shop.shop.domain.item.Item;
import com.shop.shop.dto.CategoryItemDTO;
import com.shop.shop.repository.CategoryItemRepository;
import com.shop.shop.repository.CategoryRepository;
import com.shop.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CategoryItemServiceImpl implements CategoryItemService {

    private final CategoryRepository categoryRepository;
    private final CategoryItemRepository categoryItemRepository;
    private final ItemRepository itemRepository;

    // 상품과 카테고리를 연결해주는 메서드
    @Override
    public CategoryItemDTO registerCategoryItem(Item item, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("해당 카테고리를 찾을 수 없습니다."));
        Item findItem = itemRepository.findById(item.getId())
                .orElseThrow(() -> new RuntimeException("해당 아이템을 찾을 수 없습니다."));
        CategoryItem categoryItem = new CategoryItem();
        categoryItem.changeCategoryItem(category, findItem);
        CategoryItem savedCategoryItem = categoryItemRepository.save(categoryItem);
        return new CategoryItemDTO(savedCategoryItem);
    }

    // 모든 카테고리 상품 페이징 조회 - 목록+이미지
    @Override
    public Page<CategoryItemDTO> getAllCategoryItem(Pageable pageable) {
        Page<CategoryItem> categoryPage = categoryItemRepository.findAllWithItem(pageable);
        if (categoryPage == null) {
            throw new RuntimeException("해당 카테고리 상품들을 조회할 수 없습니다.");
        }
        return categoryPage.map(CategoryItemDTO::new);
    }

    // 특정 카테고리 상품 페이징 조회 - 목록+이미지
    @Override
    public Page<CategoryItemDTO> getAllItemsFromCategoryItem(Pageable pageable, Long categoryId) {
        Page<CategoryItem> categoryPage = categoryItemRepository.findAllPageByCategoryIdDESC(pageable, categoryId);
        if (categoryPage == null) {
            throw new RuntimeException("해당 카테고리 상품들을 조회할 수 없습니다.");
        }
        return categoryPage.map(CategoryItemDTO::new);
    }

    // 특정 카테고리 상품 페이징 조회 - 목록+이미지
    @Override
    public Page<CategoryItemDTO> getAllItemsFromCategoryItemWithStatus(Pageable pageable, Long categoryId,
            String categoryItemStatus) {
        Page<CategoryItem> categoryPage;
        log.info("정렬 상태: " + categoryItemStatus);

        switch (categoryItemStatus.toUpperCase()) { // 문자열 비교니까 대문자로 통일하는게 안전
            case "NEWEST":
                categoryPage = categoryItemRepository.findAllPageByCategoryIdDESC(pageable, categoryId);
                break;
            case "LATER":
                categoryPage = categoryItemRepository.findAllPageByCategoryIdASC(pageable, categoryId);
                break;
            case "HIGH_PRICE":
                categoryPage = categoryItemRepository.findAllPageByItemPriceDESC(pageable, categoryId);
                break;
            case "LOW_PRICE":
                categoryPage = categoryItemRepository.findAllPageByItemPriceASC(pageable, categoryId);
                break;
            default:
                categoryPage = categoryItemRepository.findAllPageByCategoryIdDESC(pageable, categoryId);
                break;
        }
        if (categoryPage == null) {
            throw new RuntimeException("해당 카테고리 상품들을 조회할 수 없습니다.");
        }
        return categoryPage.map(CategoryItemDTO::new);
    }

}

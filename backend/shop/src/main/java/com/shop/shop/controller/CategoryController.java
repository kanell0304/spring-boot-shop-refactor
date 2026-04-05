package com.shop.shop.controller;

import com.shop.shop.domain.category.Category;
import com.shop.shop.domain.category.CategoryItem;
import com.shop.shop.domain.item.Item;
import com.shop.shop.dto.CategoryDTO;
import com.shop.shop.dto.CategoryItemDTO;
import com.shop.shop.repository.CategoryRepository;
import com.shop.shop.repository.ItemRepository;
import com.shop.shop.service.CategoryItemService;
import com.shop.shop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final CategoryItemService categoryItemService;
    private final ItemRepository itemRepository;

    // 카테고리 등록
    @PostMapping("/add")
    public ResponseEntity<CategoryDTO> registerCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategory = categoryService.registerCategory(categoryDTO);
        return ResponseEntity.ok(savedCategory);
    }

    // 카테고리 모두 조회
    @GetMapping("/list")
    public ResponseEntity<List<Category>> getAllCategory() {
        List<Category> category = categoryRepository.findAllParentCategory();
        return ResponseEntity.ok(category);
    }

    // 카테고리 모두 조회(페이징)
    @GetMapping("/listPage")
    public ResponseEntity<Page<Category>> getAllCategoryWithPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> category = categoryService.getAllCategory(pageable);
        return ResponseEntity.ok(category);
    }

    // Id를 기준으로 특정 카테고리 조회
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable("id") Long id) {
        // 먼저 부모 카테고리에서 시도
        Category category = categoryRepository.findOneParentCategory(id);

        // 부모가 아니면 일반 findById로 조회 시도
        if (category == null) {
            category = categoryRepository.findById(id).orElse(null);
        }

        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new CategoryDTO(category));
    }

    // 특정 카테고리 조회 (페이징)
    @GetMapping("/page/{id}")
    public ResponseEntity<Page<Category>> getCategoryWithPage(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> category = categoryService.getAllItemsFromCategory(pageable, id);
        return ResponseEntity.ok(category);
    }

    // 카테고리 수정
    @PutMapping("/edit")
    public ResponseEntity<CategoryDTO> editCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO editedCategory = categoryService.editCategory(categoryDTO);
        return ResponseEntity.ok(editedCategory);
    }

    // 카테고리 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            Map<String, String> response = Map.of("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = Map.of("result", "fail", "error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 특정 상품을 카테고리에 등록
    @PostMapping("/addCategoryItem/{itemId}/{categoryId}")
    public ResponseEntity<Map<String, String>> registerCategoryItem(
            @PathVariable("itemId") Long itemId,
            @PathVariable("categoryId") Long categoryId) {
        try {
            Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));
            CategoryItemDTO categoryItemDTO = categoryItemService.registerCategoryItem(item, categoryId); // 카테고리에 해당
                                                                                                          // 아이템 등록
            Map<String, String> response = Map.of("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = Map.of("result", "fail", "error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 모든 카테고리 상품 조회(페이징)
    @GetMapping("/categoryItemPage")
    public ResponseEntity<Page<CategoryItemDTO>> getAllCategoryItem(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryItemDTO> categoryItemDTOPage = categoryItemService.getAllCategoryItem(pageable);
        return ResponseEntity.ok(categoryItemDTOPage);
    }

    // 특정 카테고리 상품 조회(페이징)
    @GetMapping("/categoryItemPage/{id}")
    public ResponseEntity<Page<CategoryItemDTO>> getALLCategoryItemById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryItemDTO> categoryItemDTOPage = categoryItemService.getAllItemsFromCategoryItem(pageable, id);
        return ResponseEntity.ok(categoryItemDTOPage);
    }

    // 특정 카테고리 상품 조회(페이징) 필터 기능 추가
    // @GetMapping("/categoryItemPageWithStatus")
    // public ResponseEntity<Page<CategoryItemDTO>> getALLCategoryItemByIdAndStatus(
    // @RequestBody CategoryItemDTO categoryItemDTO,
    // @RequestParam(defaultValue = "0") int page,
    // @RequestParam(defaultValue = "10") int size) {
    // Pageable pageable = PageRequest.of(page, size);
    // Page<CategoryItemDTO> categoryItemDTOPage =
    // categoryItemService.getAllItemsFromCategoryItemWithStatus(pageable,
    // categoryItemDTO);
    // return ResponseEntity.ok(categoryItemDTOPage);
    // }

    // 카테고리 상태를 기준으로 카테고리 상품 조회
    @GetMapping("/categoryItemPageWithStatus")
    public ResponseEntity<Page<CategoryItemDTO>> getAllCategoryItemsWithStatus(
            @RequestParam Long categoryId,
            @RequestParam(defaultValue = "NEWEST") String categoryItemStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryItemDTO> result = categoryItemService.getAllItemsFromCategoryItemWithStatus(pageable, categoryId,
                categoryItemStatus);
        return ResponseEntity.ok(result);
    }
}

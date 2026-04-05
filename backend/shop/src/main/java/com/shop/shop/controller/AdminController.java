package com.shop.shop.controller;

import com.shop.shop.domain.item.Item;
import com.shop.shop.dto.CategoryDTO;
import com.shop.shop.dto.CategoryItemDTO;
import com.shop.shop.dto.OrderDTO;
import com.shop.shop.dto.QnAListDTO;
import com.shop.shop.repository.CategoryRepository;
import com.shop.shop.repository.ItemRepository;
import com.shop.shop.service.CategoryItemService;
import com.shop.shop.service.CategoryService;
import com.shop.shop.service.OrderService;
import com.shop.shop.service.QnAListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/admin")
public class AdminController {

    private final OrderService orderService;
    private final QnAListService qnAListService;

    private final CategoryService categoryService;
    private final CategoryItemService categoryItemService;
    private final ItemRepository itemRepository;

    // 모든 주문 조회
    @GetMapping("/getOrderList")
    public ResponseEntity<Page<OrderDTO>> getAllOrder(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDTO> getOrderPage = orderService.findAllOrders(pageable);
        return ResponseEntity.ok(getOrderPage);
    }

    // 질의응답 리스트 모두 조회(페이징) 삭제 포함
    @GetMapping("/listPage")
    public ResponseEntity<Page<QnAListDTO>> getAllQnAListPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<QnAListDTO> qnAListDTOPage = qnAListService.getAllQnAListPage(pageable);
        if (qnAListDTOPage == null) {
            throw new RuntimeException("조회된 질의응답 페이지가 없습니다.");
        }
        return ResponseEntity.ok(qnAListDTOPage);
    }

    // 카테고리 등록
    @PostMapping("/add")
    public ResponseEntity<CategoryDTO> registerCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategory = categoryService.registerCategory(categoryDTO);
        return ResponseEntity.ok(savedCategory);
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

}

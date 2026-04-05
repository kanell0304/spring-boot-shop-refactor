package com.shop.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.shop.domain.category.Category;
import com.shop.shop.domain.item.Item;
import com.shop.shop.domain.item.ItemOption;
import com.shop.shop.dto.*;
import com.shop.shop.repository.CategoryRepository;
import com.shop.shop.service.*;
import com.shop.shop.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/items")
public class ItemController {

    private final CustomFileUtil fileUtil;
    private final CategoryRepository categoryRepository;
    private final CategoryItemService categoryItemService;
    private final ItemService itemService;
    private final ItemServiceImpl itemServiceImpl;

    // 페이징 목록 조회
    @GetMapping("/list")
    public ResponseEntity<Page<ItemDTO>> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(itemService.getAllItems(pageable));
    }

    // 단일 데이터 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getItem(@PathVariable Long id) {
        try {
            ItemDTO itemDTO = itemService.getOne(id);
            return ResponseEntity.ok(itemDTO);
        } catch (IllegalArgumentException e) {
            // 404 error
            Long itemId = itemService.getOne(id).getId();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("result", "fail", "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("result", "fail", "error", e.getMessage()));
        }
    }

    // 페이징 목록 조회(아이템 정보 + 썹네일 이미지 + 옵션 + 인포)
    @GetMapping("/listPage")
    public ResponseEntity<Page<ItemDTO>> getAllItemsWithImageAndOptionsAndInfo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(itemService.getAllItemsWithImageAndOptionsAndInfo(pageable));
    }

    // 특정 아이템의 이미지 리스트 조회 API (별도 서비스 분리 X)
    @GetMapping("/view/{fileName}")
    public ResponseEntity<?> getItemImages(@PathVariable String fileName) {
        try {
            ResponseEntity<Resource> imageResponse = itemService.getImageUrlByFileName(fileName);
            if (imageResponse == null || !imageResponse.hasBody()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("result", "fail", "error", "해당 파일을 찾을 수 없습니다."));
            }
            return imageResponse;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("result", "fail", "error", e.getMessage()));
        }
    }

    // 특정 아이템의 옵션 모두 조회
    @GetMapping("/itemOption/{itemId}")
    public ResponseEntity<List<ItemOptionDTO>> getItemOptions(@PathVariable Long itemId) {
        List<ItemOptionDTO> itemOptionDTOList = itemService.getItemOptionByItemId(itemId);
        return ResponseEntity.ok(itemOptionDTOList);
    }

    // 아이템 등록
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemDTO> registerItem(
            @RequestPart("itemDTO") ItemDTO itemDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestParam("categoryId") Long categoryId
    ) {
        try {
            if (categoryId == null) {
                throw new RuntimeException("등록하려는 카테고리Id를 입력해주세요.");
            }
    
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("해당 카테고리를 찾을 수 없습니다."));
    
            // 파일 저장 처리
            if (files != null && !files.isEmpty()) {
                List<String> uploadFileNames = fileUtil.saveFiles(files);
                itemDTO.setUploadFileNames(uploadFileNames);
            }
    
            // 서비스 호출
            ItemDTO createdItem = itemService.createItem(itemDTO, files, categoryId);
            Item item = itemServiceImpl.getSavedItem();
            CategoryItemDTO categoryItemDTO = categoryItemService.registerCategoryItem(item, categoryId);
    
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } catch (Exception e) {
            e.printStackTrace(); // 에러 추적 로그
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    

    // 아이템 수정
    @PutMapping(value = "/modify/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemDTO> updateItem(
            @PathVariable("id") Long id,
            @RequestPart("itemDTO") ItemDTO itemDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        System.out.println("파일 수: " + (files != null ? files.size() : 0));
        return ResponseEntity.ok(itemService.updateItem(id, itemDTO, files));
    };


    // 아이템 삭제 (논리적 삭제)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteItem(@PathVariable Long id) {
        try {
            itemService.deleteItem(id);
            Map<String, String> response = Map.of("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = Map.of("result", "fail", "error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 아이템 등록 (PostMan)
    @PostMapping(value = "/addPost")
    public ResponseEntity<ItemDTO> registerItem(
            @RequestPart(value = "itemDTO") String itemDTOJson, // String으로 받아서 JSON 파싱
            @RequestParam(value = "categoryId") Long categoryId, // categoryId
            @RequestPart(value = "files", required = false) List<MultipartFile> files // 파일들
    ) {
        try {
            // JSON을 ItemDTO 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            ItemDTO itemDTO = objectMapper.readValue(itemDTOJson, ItemDTO.class);

            // 카테고리 검증
            if (categoryId == null) {
                throw new RuntimeException("카테고리 ID를 입력해주세요.");
            }

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("해당 카테고리를 찾을 수 없습니다."));

            // 파일 저장 처리
            if (files != null && !files.isEmpty()) {
                List<String> uploadFileNames = fileUtil.saveFiles(files);
                itemDTO.setUploadFileNames(uploadFileNames);  // 파일 이름 저장
            }

            // 서비스 호출
            ItemDTO createdItem = itemService.createItem(itemDTO, files, categoryId);  // 상품 생성
            Item item = itemServiceImpl.getSavedItem();
            CategoryItemDTO categoryItemDTO = categoryItemService.registerCategoryItem(item, categoryId);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } catch (Exception e) {
            e.printStackTrace(); // 에러 추적 로그
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 아이템 수정 (PostMan)
    @PutMapping(value = "/modifyPost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemDTO> updateItemFromPostMan(
            @RequestPart(value = "itemDTO") String itemDTOJson, //
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ItemDTO itemDTO = objectMapper.readValue(itemDTOJson, ItemDTO.class);

        System.out.println("파일 수: " + (files != null ? files.size() : 0));
        return ResponseEntity.ok(itemService.updateItem(itemDTO.getId(), itemDTO, files));
    };

}

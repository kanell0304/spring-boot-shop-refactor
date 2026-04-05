package com.shop.shop.controller;

import com.shop.shop.dto.WishListDTO;
import com.shop.shop.repository.WishListRepository;
import com.shop.shop.service.WishListService;
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
@RequestMapping("/api/wish")
public class WishListController {

    private final WishListRepository wishListRepository;
    private final WishListService wishListService;

    // 관심 등록
    @PostMapping("/add")
    public ResponseEntity<?> registerInterest(@RequestBody WishListDTO wishListDTO) {
        WishListDTO result = wishListService.registerInterest(wishListDTO);

        if (result == null) {
            return ResponseEntity.ok("삭제됨");
        }
        return ResponseEntity.ok(result);
    }

    // 특정 회원 관심 목록 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<List<WishListDTO>> getWishListByMember(@PathVariable Long memberId) {
        List<WishListDTO> wishList = wishListService.getWishListByMemberId(memberId);
        return ResponseEntity.ok(wishList);
    }

    // 특정 회원 관심 목록 조회(페이징)
    @GetMapping("/page/{memberId}")
    public ResponseEntity<Page<WishListDTO>> getWishListByMember(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WishListDTO> wishList = wishListService.getWishListByMemberId(pageable, memberId);
        return ResponseEntity.ok(wishList);
    }

    // 관심 상품 삭제
    @DeleteMapping("/{wishListId}")
    public ResponseEntity<?> deleteItemFromWishList(@PathVariable("wishListId") Long wishListId) {
        try {
            wishListService.deleteItemFromWishList(wishListId);
            Map<String, String> response = Map.of("result", "success");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("result", "fail", "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("result", "fail", "error", e.getMessage()));
        }
    }

    // 다중 삭제(선택한 상품 삭제)
    @DeleteMapping("/multipleDelete")
    public ResponseEntity<?> multipleDelete(@RequestBody WishListDTO wishListDTO) {
        try {
            wishListService.multipleDeleteItemFromWishList(wishListDTO);
            Map<String, String> response = Map.of("result", "success");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("result", "fail", "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("result", "fail", "error", e.getMessage()));
        }
    }
}
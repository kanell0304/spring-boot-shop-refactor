package com.shop.shop.controller;

import com.shop.shop.dto.CartDTO;
import com.shop.shop.dto.CheckDTO;
import com.shop.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// 테스트
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    // 회원Id를 기준으로 장바구니에 상품 등록
    @PostMapping("/add")
    public ResponseEntity<CartDTO> registerCart(@RequestBody CartDTO cartDTO) {
        CartDTO registerCart = cartService.registerCart(cartDTO);
        return ResponseEntity.ok(registerCart);
    }

    // 회원Id를 기준으로 장바구니 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<List<CartDTO>> getCartByMemberId(@PathVariable("memberId") Long memberId) {
        List<CartDTO> getCartByMemberId = cartService.getCartList(memberId);
        return ResponseEntity.ok(getCartByMemberId);
    }

    // 회원Id를 기준으로 선택된 상품들만 장바구니 조회
    @GetMapping("/checkItem/{memberId}")
    public ResponseEntity<List<CartDTO>> getCartByMemberIdWithCheckItem(@PathVariable("memberId") Long memberId) {
        List<CartDTO> getCartByMemberId = cartService.getCartListWithCheckItem(memberId);
        return ResponseEntity.ok(getCartByMemberId);
    }

    // 회원Id와 옵션Id를 기준으로 선택된 상품들만 장바구니 조회
    @PostMapping("/getSelectList")
    public ResponseEntity<List<CartDTO>> getCartByMemberIdWithOptionId(@RequestBody CartDTO cartDTO) {
        List<CartDTO> getCartByMemberId = cartService.getCartListByMemberIdANDOptionId(cartDTO);
        return ResponseEntity.ok(getCartByMemberId);
    }

    // 회원Id를 기준으로 선택된 상품들의 상태를 수정
    @PostMapping("/editSelectList")
    public ResponseEntity<List<CartDTO>> editCartListByMemberIdANDOptionId(@RequestBody CartDTO cartDTO) {
        List<CartDTO> getCartByMemberIdANDItemId = cartService.editCartListByMemberIdANDOptionId(cartDTO);
        return ResponseEntity.ok(getCartByMemberIdANDItemId);
    }

    // 회원Id를 기준으로 장바구니 상품 삭제
    @DeleteMapping("/deleteItem")
    public ResponseEntity<Map<String, String>> deleteCartItem(@RequestBody CartDTO cartDTO) {
        try {
            cartService.deleteCartItem(cartDTO);
            Map<String, String> response = Map.of("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = Map.of("result", "fail", "error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 다중 삭제(선택한 상품 삭제)
    @DeleteMapping("/multipleDelete")
    public ResponseEntity<?> multipleDelete(@RequestBody CartDTO cartDTO) {
        try {
            cartService.multipleDeleteItemFromWishList(cartDTO);
            Map<String, String> response = Map.of("result", "success");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("result", "fail", "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("result", "fail", "error", e.getMessage()));
        }
    }

    // 재고량 체크 true(가능) / false(불가) 반환
    @GetMapping("/checkQty")
    public ResponseEntity<CheckDTO> checkQty(
            @RequestParam Long optionId,
            @RequestParam int qty) {
        CheckDTO checkResult = cartService.checkOptionQty(optionId, qty);
        return ResponseEntity.ok(checkResult);
    }

    // 옵션 재고량 수정
    @PutMapping("/updateQty")
    public ResponseEntity<CheckDTO> updateQty(
            @RequestParam Long optionId,
            @RequestParam int changeQty // 예) +1 이면 증가, -1 이면 감소
    ) {
        CheckDTO result = cartService.updateOptionQty(optionId, changeQty);
        return ResponseEntity.ok(result);
    }

    // 장바구니 옵션 재고량 수정
    @PutMapping("/updateCartQty")
    public ResponseEntity<Void> updateCartQty(
            @RequestParam Long cartId,
            @RequestParam int newQty) {
        cartService.updateCartQty(cartId, newQty);
        return ResponseEntity.ok().build();
    }

}

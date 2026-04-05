package com.shop.shop.controller;

import com.shop.shop.dto.DeliveryDTO;
import com.shop.shop.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    // 배송상태 수정
    @PutMapping("/editStatus")
    public ResponseEntity<DeliveryDTO> editDeliveryStatus(@RequestBody DeliveryDTO deliveryDTO) {
        DeliveryDTO editDelivery = deliveryService.editStatus(deliveryDTO);
        if (editDelivery == null) {
            throw new RuntimeException("배송상태 변경에 실패하였습니다.");
        }
        return ResponseEntity.ok(editDelivery);
    }

}

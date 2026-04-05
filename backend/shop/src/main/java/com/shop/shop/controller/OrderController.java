package com.shop.shop.controller;

import com.shop.shop.domain.order.Order;
import com.shop.shop.dto.OrderDTO;
import com.shop.shop.repository.OrderItemRepository;
import com.shop.shop.repository.OrderRepository;
import com.shop.shop.service.OrderService;
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
@RequestMapping("/api/order")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final OrderItemRepository orderItemRepository;
    // private final OrderItemService orderItemService;

    // 주문 생성
    @PostMapping("/add")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        if (createdOrder == null) {
            throw new RuntimeException("주문생성이 제대로 이루어지지 않았습니다.");
        }
        return ResponseEntity.ok(createdOrder);
    }

    // 모든 주문 조회
    @GetMapping("/getOrderList")
    public ResponseEntity<Page<OrderDTO>> getAllOrder(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDTO> getOrderPage = orderService.findAllOrders(pageable);
        return ResponseEntity.ok(getOrderPage);
    }

    // 특정 주문Id를 기준으로 주문 상세 조회
    @GetMapping("/getOrderById/{orderId}")
    public ResponseEntity<OrderDTO> getOrderByOrderId(@PathVariable("orderId") Long orderId) {
        OrderDTO getOrder = orderService.findByOrderId(orderId);
        if (getOrder == null) {
            throw new RuntimeException("해당 주문 내역이 존재하지 않습니다.");
        }

        return ResponseEntity.ok(getOrder);
    }

    // 회원Id 를 기준으로 주문 내역 모두 조회
    @GetMapping("/getOrderWithMemberId/{memberId}")
    public ResponseEntity<List<OrderDTO>> getOrderListByMemberId(@PathVariable Long memberId) {
        List<OrderDTO> getOrderList = orderService.findAllByMemberId(memberId);
        if (getOrderList == null || getOrderList.isEmpty()) {
            throw new RuntimeException("해당 회원의 주문 내역이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(getOrderList);
    }

    // 회원Id 를 기준으로 주문 내역 모두 조회(페이징)
    @GetMapping("/getOrderWithMemberIdPage/{memberId}")
    public ResponseEntity<Page<OrderDTO>> getOrderListByMemberId(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDTO> getOrderList = orderService.findAllByMemberId(pageable, memberId);
        if (getOrderList == null || getOrderList.isEmpty()) {
            throw new RuntimeException("해당 회원의 주문 내역이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(getOrderList);
    }

    // 배송Id를 기준으로 주문 내역 조회
    @GetMapping("/get/{deliveryId}")
    public ResponseEntity<OrderDTO> getOrderByDeliveryId(@PathVariable("deliveryId") Long deliveryId) {
        OrderDTO getOrder = orderService.findByDeliveryId(deliveryId);
        if (getOrder == null) {
            throw new RuntimeException("해당 주문을 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(getOrder);
    }

    // 특정 기간동안의 주문 내역 모두 조회
    @PostMapping("/duringDate")
    public ResponseEntity<List<OrderDTO>> getOrderListByDuringDate(@RequestBody OrderDTO orderDTO) {
        List<OrderDTO> getOrderList = orderService.findByDuringPeriod(orderDTO.getStartDate(), orderDTO.getEndDate());
        if (getOrderList == null || getOrderList.isEmpty()) {
            throw new RuntimeException("해당 기간동안 조회된 주문 내역이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(getOrderList);
    }

    // 특정 기간동안의 주문 내역 모두 조회(페이징)
    @PostMapping("/duringDatePage")
    public ResponseEntity<Page<OrderDTO>> getOrderListByDuringDate(
            @RequestBody OrderDTO orderDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDTO> getOrderList = orderService.findByDuringPeriod(pageable, orderDTO.getStartDate(),
                orderDTO.getEndDate());
        if (getOrderList == null || getOrderList.isEmpty()) {
            throw new RuntimeException("해당 기간동안 조회된 주문 내역이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(getOrderList);
    }

    // 특정 회원의 특정 기간동안의 주문 내역 모두 조회
    @PostMapping("/duringDateMember")
    public ResponseEntity<List<OrderDTO>> getOrderListByDuringMember(@RequestBody OrderDTO orderDTO) {
        List<OrderDTO> getOrderList = orderService.findByDuringPeriodFromMemberId(orderDTO.getMemberId(),
                orderDTO.getStartDate(), orderDTO.getEndDate());
        if (getOrderList == null || getOrderList.isEmpty()) {
            throw new RuntimeException("해당 회원 또는 해당 기간동안 조회된 주문 내역이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(getOrderList);
    }

    // 특정 회원의 특정 기간동안의 주문 내역 모두 조회(페이징)
    @PostMapping("/duringDateMemberPage")
    public ResponseEntity<Page<OrderDTO>> getOrderListByDuringMember(
            @RequestBody OrderDTO orderDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDTO> getOrderList = orderService.findByDuringPeriodFromMemberId(pageable, orderDTO.getMemberId(),
                orderDTO.getStartDate(), orderDTO.getEndDate());
        if (getOrderList == null || getOrderList.isEmpty()) {
            throw new RuntimeException("해당 회원 또는 해당 기간동안 조회된 주문 내역이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(getOrderList);
    }

    // 특정 주문배송지 수정
    @PutMapping("/edit")
    public ResponseEntity<OrderDTO> editOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO editedOrder = orderService.editOrder(orderDTO);
        if (editedOrder == null) {
            throw new RuntimeException("주문 수정에 실패했습니다.");
        }
        return ResponseEntity.ok(editedOrder);
    }

    // 특정 주문 주문 상태 수정
    @PutMapping("/editStatus")
    public ResponseEntity<OrderDTO> editOrderStatus(@RequestBody OrderDTO orderDTO) {
        OrderDTO editedOrder = orderService.editOrderStatus(orderDTO);
        if (editedOrder == null) {
            throw new RuntimeException("주문 상태 수정에 실패했습니다.");
        }
        return ResponseEntity.ok(editedOrder);
    }

    // 주문 삭제(논리적 삭제)
    @DeleteMapping("delete/{orderId}")
    public ResponseEntity<?> multipleDelete(@PathVariable("orderId") Long orderId) {
        try {
            orderService.deleteOrder(orderId);
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

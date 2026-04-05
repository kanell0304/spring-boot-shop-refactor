package com.shop.shop.domain.order;

public enum OrderStatus {
    PENDING,            // 주문o, 결제x
    PAID,               // 결제 완료
    PREPARING,          // 상품 준비중
    SHIPPED,            // 배송 시작
    DELIVERED,          // 배송 완료
    CANCEL,             // 주문 취소
    RETURN_REQUESTED,   // 반품 요청
    RETURNED,           // 반품 완료
    EXCHANGE_REQUESTED, // 교환 요청
    EXCHANGED,          // 교환 완료
    FAILED;             // 주문 실패
}

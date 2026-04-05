package com.shop.shop.domain.delivery;

public enum DeliveryStatus {

    PENDING,                // 주문 완료
    PREPARING,              // 배송 준비중
    SHIPPED,                // 배송 시작 - 택배사 전달
    OUT_FOR_DELIVERY,       // 배송 시작 - 택배기사가 물건을 가지고 출발
    DELIVERED,              // 배송 완료
    DELIVERY_FAILED,        // 배송 실패
    RETURN_SHIPPING,        // 반품 배송중
    RETURNED,               // 반품 완료
    EXCHANGE_SHIPPING,      // 교환상품 배송중
    EXCHANGED;              // 교환 완료

}

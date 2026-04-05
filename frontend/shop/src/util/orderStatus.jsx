const orderStatusMap = {
    PENDING: "무통장 입금(결제 대기중)",
    PAID: "결제완료",
    PREPARING: "상품 준비중",
    SHIPPED: "배송 시작",
    DELIVERED: "배송 완료",
    CANCEL: "주문취소",
    RETURN_REQUESTED: "반품요청",
    RETURNED: "반품완료",
    EXCHANGE_REQUESTED: "교환요청",
    EXCHANGED: "교환완료",
    FAILED: "주문실패",
  };
  export const getOrderStatusText = (status) => orderStatusMap[status] || "알 수 없음";
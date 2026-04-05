package com.shop.shop.dto;

import com.shop.shop.domain.delivery.DeliveryStatus;
import com.shop.shop.domain.member.MileageStatus;
import com.shop.shop.domain.order.Order;
import com.shop.shop.domain.order.OrderItem;
import com.shop.shop.dto.OrderItemDTO;
import com.shop.shop.domain.order.OrderStatus;
import com.shop.shop.domain.order.PaymentMethod;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;
    private Long memberId;
    private String memberEmail;
    private LocalDateTime orderDate;
    private int totalAmount;
    private OrderStatus orderStatus;
    private boolean delFlag;
    private String payerName;
    private String payerNumber;
    private String orderRequest;
    private PaymentMethod paymentMethod;
    private String recipientName;
    private String recipientNumber;
    private String recipient_zip_code;
    private String recipient_default_address;
    private String recipient_detailed_address;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private DeliveryStatus deliveryStatus;
    private MileageStatus mileageStatus;
    private int addMileageAmount; // 마일리지 적립량
    private int usingMileage;

    // 다중 선택에 필요한 ItemId를 담아두는 변수
    private Long[] selectId;

    private List<OrderItemDTO> orderItemList = new ArrayList<>();

    // Order를 OrderDTO로 변환
    public OrderDTO(Order order, List<OrderItem> items) {
        this.id = order.getId();
        this.memberId = order.getMember().getId();
        this.memberEmail = order.getMember().getEmail();
        this.orderDate = order.getOrderDate();
        this.totalAmount = order.getTotalAmount();
        this.orderStatus = order.getOrderStatus();
        this.delFlag = order.isDelFlag();
        this.payerName = order.getPayerName();
        this.payerNumber = order.getPayerNumber();
        this.orderRequest = order.getOrderRequest();
        this.paymentMethod = order.getPaymentMethod();
        this.recipientName = order.getRecipientName();
        this.recipientNumber = order.getRecipientNumber();
        this.recipient_zip_code = order.getRecipient_zip_code();
        this.recipient_default_address = order.getRecipient_default_address();
        this.recipient_detailed_address = order.getRecipient_detailed_address();
        this.deliveryStatus = order.getDelivery().getStatus();
        this.mileageStatus = order.getMileageStatus();
        this.addMileageAmount = order.getAddMileageAmount();
        this.usingMileage = order.getUsingMileage();

        this.orderItemList = (items != null ? items : new ArrayList<>()).stream()
                .map(item -> new OrderItemDTO((OrderItem) item))  // 대신 람다식으로 명시적 호출
                .collect(Collectors.toList());
    }

    // Order를 OrderDTO로 변환
    public OrderDTO(Order order) {
        this.id = order.getId();
        this.memberId = order.getMember().getId();
        this.memberEmail = order.getMember().getEmail();
        this.orderDate = order.getOrderDate();
        this.totalAmount = order.getTotalAmount();
        this.orderStatus = order.getOrderStatus();
        this.delFlag = order.isDelFlag();
        this.payerName = order.getPayerName();
        this.payerNumber = order.getPayerNumber();
        this.orderRequest = order.getOrderRequest();
        this.paymentMethod = order.getPaymentMethod();
        this.recipientName = order.getRecipientName();
        this.recipientNumber = order.getRecipientNumber();
        this.recipient_zip_code = order.getRecipient_zip_code();
        this.recipient_default_address = order.getRecipient_default_address();
        this.recipient_detailed_address = order.getRecipient_detailed_address();
        this.deliveryStatus = order.getDelivery().getStatus();
        this.mileageStatus = order.getMileageStatus();
        this.addMileageAmount = order.getAddMileageAmount();
        this.usingMileage = order.getUsingMileage();
    }

}
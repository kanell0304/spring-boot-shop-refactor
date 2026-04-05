package com.shop.shop.dto;

import com.shop.shop.domain.order.Order;
import com.shop.shop.domain.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private int orderPrice;
    private int qty;
    private Long itemId;
    private String itemName;
    private int discountRate;
    private int discountPrice;
    private Long itemOptionId;
    private Long orderId;
    private String optionName;
    private String optionValue;
    private Long itemImageId;
    private String imageName;

    public OrderItemDTO(OrderItem orderItem) {
        if (orderItem != null) {
            this.orderPrice = orderItem.getOrderPrice();
            this.qty = orderItem.getQty();
            this.discountRate = orderItem.getItem().getDiscountRate();
            this.discountPrice = orderItem.getDiscountPrice();
            this.itemId = (orderItem.getItem() != null) ? orderItem.getItem().getId() : null;
            this.itemName = (orderItem.getItem().getName() != null) ? orderItem.getItem().getName() : null;
            this.itemOptionId = (orderItem.getItemOption() != null) ? orderItem.getItemOption().getId() : null;
            this.orderId = (orderItem.getOrder() != null) ? orderItem.getOrder().getId() : null;
            this.optionName = (orderItem.getItemOption().getOptionName() != null) ? orderItem.getItemOption().getOptionName() : null;
            this.optionValue = (orderItem.getItemOption().getOptionValue() != null) ? orderItem.getItemOption().getOptionValue() : null;
            this.itemImageId = (orderItem.getItemImage() != null) ? orderItem.getItemImage().getId() : null;
            this.imageName = (orderItem.getItemImage() != null && orderItem.getItemImage().getFileName() != null) ? orderItem.getItemImage().getFileName() : "default.png";
        }
    }
    
}

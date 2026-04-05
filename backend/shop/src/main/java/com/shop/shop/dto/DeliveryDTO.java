package com.shop.shop.dto;

import com.shop.shop.domain.delivery.Delivery;
import com.shop.shop.domain.delivery.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {

    private Long id;
    private DeliveryStatus deliveryStatus;

    public DeliveryDTO(Delivery delivery) {
        this.id = delivery.getId();
        this.deliveryStatus = delivery.getStatus();
    }

}

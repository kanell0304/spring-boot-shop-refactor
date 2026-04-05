package com.shop.shop.dto;

import com.shop.shop.domain.member.Member;
import com.shop.shop.domain.member.Mileage;
import com.shop.shop.domain.member.MileageStatus;
import com.shop.shop.domain.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MileageDTO {

    private Long id;
    private int amount;
    private LocalDateTime mileageDate;
    private MileageStatus mileageStatus;

    private Long memberId;
    private Long orderId;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public MileageDTO(Mileage mileage) {
        this.id = mileage.getId();
        this.amount = mileage.getAmount();
        this.mileageDate = mileage.getMileageDate();
        this.mileageStatus = mileage.getMileageStatus();
        this.memberId = mileage.getMember().getId();
        this.orderId = mileage.getOrder().getId();
    }

}

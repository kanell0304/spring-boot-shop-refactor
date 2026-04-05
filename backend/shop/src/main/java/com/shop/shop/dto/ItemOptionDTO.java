package com.shop.shop.dto;

import com.shop.shop.domain.item.ItemOption;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemOptionDTO {
    @Column(name = "option_id")
    private Long optionId;

    @Column(name = "option_name")
    private String optionName;

    @Column(name = "option_value")
    private String optionValue;

    private int optionPrice;

    @Column(name = "stockQty")
    private int stockQty;

    public ItemOptionDTO(ItemOption itemOption) {
        this.optionId = itemOption.getId();
        this.optionName = itemOption.getOptionName();
        this.optionValue = itemOption.getOptionValue();
        this.optionPrice = itemOption.getOptionPrice();
        this.stockQty = itemOption.getStockQty();
    }

}

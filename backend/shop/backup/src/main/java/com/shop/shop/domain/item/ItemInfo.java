package com.shop.shop.domain.item;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Item_Info")
public class ItemInfo {

    private String infoKey;
    private String infoValue;

}
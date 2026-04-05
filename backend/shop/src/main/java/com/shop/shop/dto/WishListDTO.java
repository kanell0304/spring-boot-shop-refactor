package com.shop.shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.shop.domain.cart.WishList;
import com.shop.shop.domain.item.Item;
import com.shop.shop.domain.item.ItemImage;
import com.shop.shop.domain.member.Member;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishListDTO {

    @Column(name = "wish_list_id")
    private Long wishListId;

    private Long memberId;

    private Long itemId;
    private String itemName;
    private int itemPrice;
    private int itemDiscountRate;
    private float itemScore;
    private LocalDateTime dueDate;
    private String itemImage;

    private Long[] deleteId;

    public WishListDTO(WishList wishList) {
        this.wishListId = wishList.getId();
        this.memberId = wishList.getMember().getId();

        Item item = wishList.getItem();
        this.itemId = item.getId();
        this.itemName = item.getName();
        this.itemPrice = item.getPrice();
        this.itemDiscountRate = item.getDiscountRate();
        this.itemScore = item.getTotalScore();
        this.dueDate = item.getDueDate();
        this.itemImage = extractItemImage(item);
    }

    private String extractItemImage(Item item) {
        return item.getImages().stream()
                .filter(img -> img.getOrd() == 0)
                .map(ItemImage::getFileName)
                .findFirst()
                .orElse(null);
    }

    public void changeItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

}

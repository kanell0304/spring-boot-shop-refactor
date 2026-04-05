package com.shop.shop.dto;

import com.shop.shop.domain.cart.Cart;
import com.shop.shop.domain.category.Category;
import com.shop.shop.domain.item.Item;
import com.shop.shop.domain.member.Member;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private Long cartId;
    private Long memberId;
    private int qty;

    private Long itemId;
    private String itemName;
    private int itemPrice;
    private int discountRate;

    private Long optionId;
    private String optionName;
    private String optionValue;
    private int optionPrice;

    // 다중 삭제에 필요한 ItemId를 담아두는 변수
    private Long[] deleteId;

    // 다중 선택에 필요한 ItemId를 담아두는 변수
    private Long[] selectId;
    
    private Long imageId;
    private String imageName;

    private boolean checkQty;

    private boolean checkItem;

    public CartDTO(Cart cart) {
        this.cartId = cart.getId();
        this.memberId = cart.getMember().getId();
        this.qty = cart.getQty();

        this.itemId = cart.getItem().getId();
        this.itemName = cart.getItem().getName();
        this.itemPrice = cart.getItem().getPrice();
        this.discountRate = cart.getItem().getDiscountRate();
        this.checkItem = cart.isCheckItem();

        if (cart.getItemImage() != null) {
            this.imageId = cart.getItemImage().getId();
            this.imageName = cart.getItemImage().getFileName();
        }

        if (cart.getItemOption() != null) {
            this.optionId = cart.getItemOption().getId();
            this.optionName = cart.getItemOption().getOptionName();
            this.optionValue = cart.getItemOption().getOptionValue();
            this.optionPrice = cart.getItemOption().getOptionPrice();
        }
    }

}

package com.shop.shop.domain.cart;

import com.shop.shop.domain.item.Item;
import com.shop.shop.domain.item.ItemImage;
import com.shop.shop.domain.item.ItemOption;
import com.shop.shop.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_option_id")
    private ItemOption itemOption; // 선택한 옵션을 저장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_image_id")
    private ItemImage itemImage; // 선택한 이미지를 저장

    private boolean incrementQty;

    private boolean checkItem;

    private int qty;

    public void registerCart(Member member, Item item, ItemOption itemOption, ItemImage itemImage) {
        this.member = member;
        this.item = item;
        this.itemOption = itemOption;
        this.itemImage = itemImage;
    }

    public void changeQty(int qty) {
        this.qty = qty;
    }

    public void changeCheckItem(boolean checkItem) {
        this.checkItem = checkItem;
    }

}

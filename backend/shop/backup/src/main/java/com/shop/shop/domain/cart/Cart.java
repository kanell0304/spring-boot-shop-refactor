package com.shop.shop.domain.cart;

import com.shop.shop.domain.item.Item;
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    @Column(nullable = false)
    private Member member;

    private int qty;

    @OneToMany(mappedBy = "id")
//    @Column(nullable = false)
    private List<Item> itemList;

    // 수량 변경
    public void changeQty(int qty) {
        this.qty = qty;
    }

    // 아이템 목록 전부 제거
    public void removeItemList() {
        itemList.clear();
    }


}

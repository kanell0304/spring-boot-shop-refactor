package com.shop.shop.domain.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.shop.domain.item.Item;
import com.shop.shop.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class CategoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_item_id")
    private Long id;

    @ManyToOne
    @JsonIgnore  // 순환 참조 방지
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public void changeCategoryItem(Category category, Item item) {
        this.category = category;
        this.item = item;
    }

}

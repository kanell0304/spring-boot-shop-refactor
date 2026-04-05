package com.shop.shop.domain.item;

import com.shop.shop.exception.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Item_Option")
public class ItemOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_option_id")
    private Long id;

    private int stockQty;

    @Column(name = "option_name")
    private String optionName;

    @Column(name = "option_value")
    private String optionValue;

    @Column(name = "item_id")
    private Long itemId;

    public ItemOption(String optionName, String optionValue) {
        this.optionName = optionName;
        this.optionValue = optionValue;
    }

    // 재고 증가
    public void addStock(int qty) {
        this.stockQty += qty;
    }

    // 재고 삭제
    public void removeStock(int qty) {
        int remainingStock = this.stockQty - qty;
        if (remainingStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQty = remainingStock;
    }

    public void changeStockQty(int stockQty) {
        this.stockQty = stockQty;
    }
}
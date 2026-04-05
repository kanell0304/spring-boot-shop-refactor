package com.shop.shop.dto;

import com.shop.shop.domain.category.CategoryItem;
import com.shop.shop.domain.category.CategoryItemStatus;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryItemDTO {

    @Column(name = "category_item_id")
    private Long categoryItemId;

    @Column(name = "category_id")
    private Long categoryId;
    private String categoryName;

    @Column(name = "item_id")
    private Long itemId;
    private String itemName;
    private String itemDescription;
    private int price;
    private float totalScore;
    private int discountRate;
    private boolean delFlag;
    private LocalDateTime dueDate;
    private int salesVolume;

    private CategoryItemStatus categoryItemStatus;

//    private List<ItemOptionDTO> options = new ArrayList<>();
//    private Map<String, String> info = new HashMap<>();
    private String uploadFileNames;

    public CategoryItemDTO(CategoryItem categoryItem) {
        this.categoryItemId = categoryItem.getId();
        this.categoryName = categoryItem.getCategory().getCategoryName();
        this.categoryId = categoryItem.getCategory().getId();
        this.itemId = categoryItem.getItem().getId();
        this.itemName = categoryItem.getItem().getName();
        this.itemDescription = categoryItem.getItem().getDescription();
        this.price = categoryItem.getItem().getPrice();
        this.totalScore = categoryItem.getItem().getTotalScore();
        this.discountRate = categoryItem.getItem().getDiscountRate();
        this.delFlag = categoryItem.getItem().isDelFlag();
        this.dueDate = categoryItem.getItem().getDueDate();
        this.salesVolume = categoryItem.getItem().getSalesVolume();
        this.uploadFileNames = (categoryItem.getItem().getImages() == null || categoryItem.getItem().getImages().isEmpty()) ? "default.png" : categoryItem.getItem().getImages().get(0).getFileName();
    }
}

package com.shop.shop.dto;

import com.shop.shop.domain.category.Category;
import com.shop.shop.domain.item.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class SearchDTO {

    private List<String> itemName;
    private List<String> categoryName;

    public SearchDTO (List<String> itemList, List<String> categoryList) {
        this.itemName = itemList;
        this.categoryName = categoryList;
    }

}

package com.shop.shop.service;

import com.shop.shop.domain.category.Category;
import com.shop.shop.domain.item.Item;
import com.shop.shop.dto.SearchDTO;
import com.shop.shop.repository.CategoryRepository;
import com.shop.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    // 입력받은 단어가 포함된 아이템 이름들을 기준으로 검색
    @Override
    public SearchDTO getSearchCategoryAndItem(String searchName) {
        List<Item> itemList = itemRepository.findAllByItemName(searchName);
        List<Category> categoryList = categoryRepository.findAllByCategoryName(searchName);

        if ((itemList == null || itemList.isEmpty()) && (categoryList == null || categoryList.isEmpty())) {
            throw new RuntimeException("해당 이름이 포함된 상품을 찾을 수 없습니다.");
        }

        List<String> itemNames = new ArrayList<>();
        for (Item item : itemList) {
            itemNames.add(item.getName());
        }
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categoryList) {
            categoryNames.add(category.getCategoryName());
        }

        return new SearchDTO(itemNames, categoryNames);
    }

}

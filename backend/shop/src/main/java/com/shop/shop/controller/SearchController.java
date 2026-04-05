package com.shop.shop.controller;

import com.shop.shop.dto.ItemDTO;
import com.shop.shop.dto.SearchDTO;
import com.shop.shop.service.ItemService;
import com.shop.shop.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    // 검색단어를 포함하는 아이템, 카테고리 이름들을 조회
    @GetMapping("/{SearchWord}")
    public ResponseEntity<SearchDTO> getSearchResultBySearchName(@PathVariable("SearchWord") String searchWord) {
        SearchDTO searchDTO = searchService.getSearchCategoryAndItem(searchWord);
        if (searchDTO == null) {
            throw new RuntimeException("검색된 결과가 없습니다.");
        }

        return ResponseEntity.ok(searchDTO);
    }

}

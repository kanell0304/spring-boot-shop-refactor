package com.shop.shop.service;


import com.shop.shop.dto.ItemDTO;
import com.shop.shop.dto.ItemOptionDTO;
import com.shop.shop.dto.WishListDTO;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {

    public ItemDTO createItem(ItemDTO itemDTO, List<MultipartFile> files, Long categoryId); // 아이템 등록

    public Page<ItemDTO> getAllItems(Pageable pageable); // 모든 아이템 PageNation 조회

    public ResponseEntity<Resource> getImageUrlByFileName(String fileName); // 파일 이미지를 통해 이미지 가져오기

    public ItemDTO getOne(Long id); // 특정 아이템 조회

    public ItemDTO updateItem(Long id, ItemDTO itemDTO, List<MultipartFile> files); // 특정 아이템 수정

    public void deleteItem(Long id); // 특정 아이템 삭제

    public Page<ItemDTO> getAllItemsWithImageAndOptionsAndInfo(Pageable pageable);

    public List<ItemOptionDTO> getItemOptionByItemId(Long itemId);

}

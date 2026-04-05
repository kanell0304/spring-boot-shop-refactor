package com.shop.shop.service;

import com.shop.shop.dto.WishListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WishListService {

    WishListDTO registerInterest(WishListDTO wishListDTO);
    public List<WishListDTO> getWishListByMemberId(Long memberId);
    public Page<WishListDTO> getWishListByMemberId(Pageable pageable, Long memberId);
    public void deleteItemFromWishList(Long wishListId);
    public void multipleDeleteItemFromWishList(WishListDTO wishListDTO);
}
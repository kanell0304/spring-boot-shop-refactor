package com.shop.shop.repository;

import com.shop.shop.domain.item.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    List<ItemImage> findByItemId(Long itemId);

    Optional<ItemImage> findByFileName(String fileName);

//    ItemImage findFirstByMemberId(Long memberId);
}

package com.shop.shop.repository;

import com.shop.shop.domain.item.ItemOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {
    List<ItemOption> findByItemId(Long itemId);

    // ItemOption 조회
    @Query("SELECT io FROM ItemOption io WHERE io.itemId IN :itemIds ORDER BY io.id DESC")
    List<ItemOption> findByItemIds(@Param("itemIds") List<Long> itemIds);
}

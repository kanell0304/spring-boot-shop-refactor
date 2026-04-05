package com.shop.shop.repository;

import com.shop.shop.domain.category.Category;
import com.shop.shop.domain.category.CategoryItem;
import com.shop.shop.domain.item.Item;
import com.shop.shop.dto.CategoryDTO;
import com.shop.shop.dto.CategoryItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryItemRepository extends JpaRepository<CategoryItem, Long> {

    // @EntityGraph(attributePaths = {"item"})
    // @Query("SELECT ci FROM CategoryItem ci WHERE ci.category.id = :categoryId")
    // // 삭제 된거 제외
    // CategoryDTO getItemsFromCategory(@Param("categoryId") Long categoryId);

    // categoryId를 기준으로 상위 카테고리 상품 모두 가져오기
    @EntityGraph(attributePaths = { "item" })
    @Query("SELECT ci FROM CategoryItem ci ORDER BY ci.id DESC")
    Page<CategoryItem> findAllWithItem(Pageable pageable);

    // categoryId 에 속한 상위/하위의 모든 데이터 가져오기 최신순
    @EntityGraph(attributePaths = { "item" })
    @Query("SELECT ci FROM CategoryItem ci WHERE ci.category.id = :categoryId OR ci.category.parent.id = :categoryId ORDER BY ci.id DESC")
    Page<CategoryItem> findAllPageByCategoryIdDESC(Pageable pageable, @Param("categoryId") Long categoryId);

    // categoryId 에 속한 상위/하위의 모든 데이터 가져오기 오랜된 순
    @EntityGraph(attributePaths = { "item" })
    @Query("SELECT ci FROM CategoryItem ci WHERE ci.category.id = :categoryId OR ci.category.parent.id = :categoryId ORDER BY ci.id ASC")
    Page<CategoryItem> findAllPageByCategoryIdASC(Pageable pageable, @Param("categoryId") Long categoryId);

    // categoryId 에 속한 상위/하위의 모든 데이터 가져오기
    @EntityGraph(attributePaths = { "item" })
    @Query("SELECT ci FROM CategoryItem ci WHERE ci.category.id = :categoryId OR ci.category.parent.id = :categoryId ORDER BY ci.item.price DESC")
    Page<CategoryItem> findAllPageByItemPriceDESC(Pageable pageable, @Param("categoryId") Long categoryId);

    // categoryId 에 속한 상위/하위의 모든 데이터 가져오기
    @EntityGraph(attributePaths = { "item" })
    @Query("SELECT ci FROM CategoryItem ci WHERE ci.category.id = :categoryId OR ci.category.parent.id = :categoryId ORDER BY ci.item.price ASC")
    Page<CategoryItem> findAllPageByItemPriceASC(Pageable pageable, @Param("categoryId") Long categoryId);

    // categoryId를 기준으로 모두 조회(최신순)
    @Query("SELECT ci FROM CategoryItem ci WHERE ci.category.id = :categoryId ORDER BY ci.id DESC")
    List<CategoryItem> findAllByCategoryId(@Param("categoryId") Long categoryId);

    // itemId를 기준으로 조회
    @Query("SELECT ci FROM CategoryItem ci WHERE ci.item.id = :itemId")
    CategoryItem findByItemId(@Param("itemId") Long itemId);

}

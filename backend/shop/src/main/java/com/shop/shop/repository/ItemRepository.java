package com.shop.shop.repository;


import com.shop.shop.domain.item.Item;
import com.shop.shop.domain.item.ItemOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // item 기본정보 + 이미지 목록 (옵션x)
    @EntityGraph(attributePaths = {"images"})
    @Query("SELECT i FROM Item i WHERE i.delFlag = false ORDER BY i.id DESC")
    // 삭제 된거 제외
    Page<Item> findAllWithImages(Pageable pageable);

    // ItemInfo (ElementCollection) 조회
    @Query("SELECT i.id, ii.infoKey, ii.infoValue FROM Item i JOIN i.info ii WHERE i.id IN :itemIds")
    List<Object[]> findInfoByItemIds(@Param("itemIds") List<Long> itemIds);

//    // 아이템 이미지, 옵션을 함께 불러오는 메서드1
//    @EntityGraph(attributePaths = {"images", "options"})
//    @Query("SELECT i FROM Item i WHERE i.delFlag = false") // 삭제 된거 제외
//    Page<Item> findAllWithImagesAndOptions(Pageable pageable);
//
//    // 아이템 전체 정보를 모두 불러오는 메서드
//    @EntityGraph(attributePaths = {"images", "options", "info"})
//    @Query("SELECT i FROM Item i WHERE i.delFlag = false")
//    Page<Item> findAllWithImagesAndOptionsAndInfo(Pageable pageable);

    // itemName 의 단어가 포함된 상품들 중 삭제가 되지 않은 상품들을 조회
    @Query("SELECT i FROM Item i WHERE i.name LIKE CONCAT('%', :itemName, '%') AND i.delFlag = false ORDER BY i.id DESC")
    List<Item> findAllByItemName(@Param("itemName") String itemName);
    
    Item findByName(String name);

}


/*
    쿼리 메서드 작성 법칙

    ~필드 기준으로 조회
    조회 : findBy ( readBy~,  getBy~,  queryBy~)
    갯수 : countBy~ (Long 반환값)
    존재여부 : existsBy~ (Boolean 반환값)
    삭제 : deleteBy~ (void)

    List<User> findByName(String name);  // name 으로 검색
    boolean existsByEmail(String email); // email 존재 여부 확인
    long countByAge(int age);            // 특정 age 의 개수 조회
    void deleteById(Long id);            // 특정 id 삭제
 */










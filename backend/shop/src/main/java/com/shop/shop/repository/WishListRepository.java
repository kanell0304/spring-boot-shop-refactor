package com.shop.shop.repository;

import com.shop.shop.domain.cart.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

    // 회원Id를 기준으로 상품 + 상품 이미지를 함께 가져오기
    @Query("SELECT wl FROM WishList wl " +
            "JOIN FETCH wl.item i " +
            "LEFT JOIN FETCH i.images " +
            "WHERE wl.member.id = :memberId ORDER BY wl.id DESC")
    List<WishList> findWithItemImagesByMemberId(@Param("memberId") Long memberId);

    // 회원Id를 기준으로 상품 + 상품 이미지를 함께 가져오기(페이징)
    @EntityGraph(attributePaths = {"item", "item.images"})
    @Query("SELECT wl FROM WishList wl WHERE wl.member.id = :memberId ORDER BY wl.id DESC")
    Page<WishList> findByMemberId(Pageable pageable, @Param("memberId") Long memberId);

    // 상품Id를 기준으로 관심상품 가져오기
    @Query("SELECT wl FROM WishList wl WHERE wl.item.id = :itemId ORDER BY wl.id DESC")
    WishList findByItemId(@Param("itemId") Long itemId);

    // 상품Id와 회원Id를 기준으로 데이터 유무 조회하기
    @Query("SELECT wl FROM WishList wl WHERE wl.item.id = :itemId AND wl.member.id = :memberId ORDER BY wl.id DESC")
    WishList existsByItemIdAndMemberId(@Param("itemId") Long itemId, @Param("memberId") Long memberId);

}

package com.shop.shop.repository;

import com.shop.shop.domain.list.ReviewList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewListRepository extends JpaRepository<ReviewList, Long> {

    // 특정 리뷰글을 이미지와 함께 조회
    @EntityGraph(attributePaths = "images")
    @Query("SELECT rl FROM ReviewList rl WHERE rl.id = :reviewId")
    public ReviewList findByIdWithReviewImages(@Param("reviewId") Long reviewId);

    // 특정 상품Id를 기준으로 모든 리뷰글을 이미지와 함께 조회(페이징) 삭제(delFlag = true) 포함
    @EntityGraph(attributePaths = "images")
    @Query("SELECT rl FROM ReviewList rl WHERE rl.item.id = :itemId")
    public Page<ReviewList> findAllByItemIdWithReviewImages(@Param("itemId") Long itemId, Pageable pageable);

    // 특정 상품Id를 기준으로 모든 리뷰글을 이미지와 함께(페이징) 조회 삭제(delFlag = true) 미포함
    @EntityGraph(attributePaths = "images")
    @Query("SELECT rl FROM ReviewList rl WHERE rl.item.id = :itemId AND rl.delFlag = false")
    public Page<ReviewList> findAllByItemIdWithReviewImagesANDDelFlag(@Param("itemId") Long itemId, Pageable pageable);

    // 모든 리뷰글을 이미지와 함께 조회(페이징) 삭제(delFlag = true) 포함
    @EntityGraph(attributePaths = "images")
    @Query("SELECT rl FROM ReviewList rl ORDER BY rl.id DESC")
    public Page<ReviewList> findAllReviewListPage(Pageable pageable);

    // 모든 리뷰글을 이미지와 함께 조회(페이징) 삭제(delFlag = true) 미포함
    @EntityGraph(attributePaths = "images")
    @Query("SELECT rl FROM ReviewList rl WHERE rl.delFlag = false ORDER BY rl.id DESC")
    public Page<ReviewList> findAllReviewListPageWithDelFlag(Pageable pageable);

}

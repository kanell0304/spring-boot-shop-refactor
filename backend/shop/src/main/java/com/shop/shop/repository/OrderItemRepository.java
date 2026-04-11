package com.shop.shop.repository;

import com.shop.shop.domain.order.OrderItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // 주문Id를 기준으로 주문 상품 조회
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId ORDER BY oi.id DESC")
    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);

    // 주문Id 목록을 기준으로 주문 상품 일괄 조회 (N+1 방지용 배치 조회)
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id IN :orderIds ORDER BY oi.id DESC")
    List<OrderItem> findByOrderIds(@Param("orderIds") List<Long> orderIds);

    // 주문 상품 조회(item, itemOption 과 함께)
    @EntityGraph(attributePaths = {"item", "itemOption"})
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId ORDER BY oi.id DESC")
    List<OrderItem> findDetailedByOrderId(@Param("orderId") Long orderId);

    // 회원Id와 상품Id를 기준으로 구매 이력 존재 여부 확인 (리뷰 작성 권한 체크용)
    @Query("SELECT COUNT(oi) > 0 FROM OrderItem oi WHERE oi.order.member.id = :memberId AND oi.item.id = :itemId AND oi.order.delFlag = false")
    boolean existsByMemberIdAndItemId(@Param("memberId") Long memberId, @Param("itemId") Long itemId);

}

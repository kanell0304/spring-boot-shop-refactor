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

    // 주문 상품 조회(item, itemOption 과 함께)
    @EntityGraph(attributePaths = {"item", "itemOption"})
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId ORDER BY oi.id DESC")
    List<OrderItem> findDetailedByOrderId(@Param("orderId") Long orderId);
    
}

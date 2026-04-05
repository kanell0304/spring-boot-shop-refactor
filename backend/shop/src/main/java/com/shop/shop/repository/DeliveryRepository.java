package com.shop.shop.repository;

import com.shop.shop.domain.delivery.Delivery;
import com.shop.shop.dto.DeliveryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

}

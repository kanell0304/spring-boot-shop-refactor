package com.shop.shop.repository;

import com.shop.shop.domain.member.Mileage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MileageRepository extends JpaRepository<Mileage, Long> {
}

package com.shop.shop.repository;

import com.shop.shop.domain.list.MagazineImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagazineImageRepository extends JpaRepository<MagazineImage, Long> {
}

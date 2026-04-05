package com.shop.shop.repository;

import com.shop.shop.domain.list.MagazineList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagazineListRepository extends JpaRepository<MagazineList, Long> {
}

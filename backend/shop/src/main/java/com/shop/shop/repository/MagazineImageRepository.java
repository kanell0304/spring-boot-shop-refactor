package com.shop.shop.repository;

import com.shop.shop.domain.list.MagazineImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MagazineImageRepository extends JpaRepository<MagazineImage, Long> {

    @Query("SELECT mi FROM MagazineImage mi WHERE mi.magazineId = :magazineId")
    public List<MagazineImage> findAllByMagazineId(@Param("magazineId") Long magazineId);

}

package com.shop.shop.repository;

import com.shop.shop.domain.list.MagazineList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MagazineListRepository extends JpaRepository<MagazineList, Long> {

    // 특정 매거진 페이지를 이미지와 함께 조회
    @EntityGraph(attributePaths = "images")
    @Query("SELECT ml FROM MagazineList ml WHERE ml.id = :magazineId")
    public MagazineList findByIdWithMagazineImages(@Param("magazineId") Long magazineId);

    // 모든 매거진 페이지를 이미지와 함께 조회(페이징) 삭제(delFlag = true) 포함
    @EntityGraph(attributePaths = "images")
    @Query("SELECT ml FROM MagazineList ml ORDER BY ml.id DESC")
    public Page<MagazineList> findAllMagazineListPage(Pageable pageable);

    // 모든 매거진 페이지를 이미지와 함께 조회(페이징) 삭제(delFlag = true) 미포함
    @EntityGraph(attributePaths = "images")
    @Query("SELECT ml FROM MagazineList ml WHERE ml.delFlag = false ORDER BY ml.id DESC")
    public Page<MagazineList> findAllMagazineListPageWithDelFlag(Pageable pageable);

}

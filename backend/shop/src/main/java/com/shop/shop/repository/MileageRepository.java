package com.shop.shop.repository;

import com.shop.shop.domain.member.Mileage;
import com.shop.shop.dto.MileageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MileageRepository extends JpaRepository<Mileage, Long> {

    // 모든 마일리지 조회
    @Query("SELECT m FROM Mileage m ORDER BY m.id DESC")
    public Page<Mileage> findAllMileagePage(Pageable pageable);

    // 회원Id를 기준으로 마일리지 내역 모두 조회
    @Query("SELECT m FROM Mileage m WHERE m.member.id = :memberId ORDER BY m.id DESC")
    public List<Mileage> findAllByMemberId(@Param("memberId") Long memberId);

    // 회원Id를 기준으로 마일리지 내역 모두 조회(페이징)
    @Query("SELECT m FROM Mileage m WHERE m.member.id = :memberId ORDER BY m.id DESC")
    public Page<Mileage> findAllByMemberId(Pageable pageable, @Param("memberId") Long memberId);

    // 주문Id를 기준으로 마일리지 내역 모두 조회
    @Query("SELECT m FROM Mileage m WHERE m.order.id = :orderId ORDER BY m.id DESC")
    public List<Mileage> findAllByOrderId(@Param("orderId") Long orderId);

    // 주문Id를 기준으로 마일리지 내역 모두 조회(페이징)
    @Query("SELECT m FROM Mileage m WHERE m.order.id = :orderId ORDER BY m.id DESC")
    public Page<Mileage> findAllByOrderId(Pageable pageable, @Param("orderId") Long orderId);

    // 특정 기간동안의 마일리지내역 조회
    @Query("SELECT m FROM Mileage m WHERE m.mileageDate BETWEEN :mileageDate1 AND :mileageDate2 ORDER BY m.id DESC")
    public List<Mileage> findByDuringPeriod(@Param("mileageDate1") LocalDateTime mileageDate1, @Param("mileageDate2") LocalDateTime mileageDate2);

    // 특정 기간동안의 마일리지내역 조회(페이징)
    @Query("SELECT m FROM Mileage m WHERE m.mileageDate BETWEEN :mileageDate1 AND :mileageDate2 ORDER BY m.id DESC")
    public Page<Mileage> findByDuringPeriod(Pageable pageable, @Param("mileageDate1") LocalDateTime mileageDate1, @Param("mileageDate2") LocalDateTime mileageDate2);

    // 특정 회원의 특정 기간동안의 마일리지내역 조회
    @Query("SELECT m FROM Mileage m WHERE m.member.id = :memberId AND m.mileageDate BETWEEN :mileageDate1 AND :mileageDate2 ORDER BY m.id DESC")
    public List<Mileage> findByDuringPeriodFromMemberId(@Param("memberId") Long memberId, @Param("mileageDate1") LocalDateTime mileageDate1, @Param("mileageDate2") LocalDateTime mileageDate2);

    // 특정 회원의 특정 기간동안의 마일리지내역 조회(페이징)
    @Query("SELECT m FROM Mileage m WHERE m.member.id = :memberId AND m.mileageDate BETWEEN :mileageDate1 AND :mileageDate2 ORDER BY m.id DESC")
    public Page<Mileage> findByDuringPeriodFromMemberId(Pageable pageable, @Param("memberId") Long memberId, @Param("mileageDate1") LocalDateTime mileageDate1, @Param("mileageDate2") LocalDateTime mileageDate2);

}

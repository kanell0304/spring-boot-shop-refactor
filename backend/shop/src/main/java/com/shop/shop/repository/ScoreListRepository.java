package com.shop.shop.repository;

import com.shop.shop.domain.list.ScoreList;
import com.shop.shop.dto.ScoreListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreListRepository extends JpaRepository<ScoreList, Long> {

    // 상품Id를 기준으로 점수 모두 조회
    @Query("SELECT sl FROM ScoreList sl WHERE sl.item.id = :itemId")
    public List<ScoreList> findAllScoreListByItemId(@Param("itemId") Long itemId);

    // 회원Id를 기준으로 점수 모두 조회
    @Query("SELECT sl FROM ScoreList sl WHERE sl.member.id = :memberId")
    public List<ScoreList> findAllScoreListByMemberId(@Param("memberId") Long memberId);

    // 상품Id와 회원Id를 기준으로 점수 조회
    @Query("SELECT sl FROM ScoreList sl WHERE sl.item.id = :itemId AND sl.member.id = :memberId")
    public ScoreList findAllScoreListByItemIdAndMemberId(@Param("itemId") Long itemId, @Param("memberId") Long memberId);

}

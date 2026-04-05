package com.shop.shop.repository;

import com.shop.shop.domain.list.EventList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventListRepository extends JpaRepository<EventList, Long> {

    // 특정 이벤트 페이지를 이미지와 함께 조회
    @EntityGraph(attributePaths = "images")
    @Query("SELECT el FROM EventList el WHERE el.id = :eventId")
    public EventList findByIdWithEventImages(@Param("eventId") Long eventId);

    // 모든 이벤트 페이지를 이미지와 함께 조회(페이징) 삭제(delFlag = true) 포함
    @EntityGraph(attributePaths = "images")
    @Query("SELECT el FROM EventList el ORDER BY el.id DESC")
    public Page<EventList> findAllEventListPage(Pageable pageable);

    // 모든 이벤트 페이지를 이미지와 함께 조회(페이징) 삭제(delFlag = true) 미포함
    @EntityGraph(attributePaths = "images")
    @Query("SELECT el FROM EventList el WHERE el.delFlag = false ORDER BY el.id DESC")
    public Page<EventList> findAllEventListPageWithDelFlag(Pageable pageable);

}

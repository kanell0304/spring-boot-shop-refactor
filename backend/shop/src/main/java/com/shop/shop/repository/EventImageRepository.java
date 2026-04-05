package com.shop.shop.repository;

import com.shop.shop.domain.list.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventImageRepository extends JpaRepository<EventImage, Long> {

    @Query("SELECT ei FROM EventImage ei WHERE ei.eventId = :eventId")
    public List<EventImage> findAllByEventId(@Param("eventId") Long eventId);

}

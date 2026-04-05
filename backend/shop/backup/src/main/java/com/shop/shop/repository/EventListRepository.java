package com.shop.shop.repository;

import com.shop.shop.domain.list.EventList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventListRepository extends JpaRepository<EventList, Long> {
}

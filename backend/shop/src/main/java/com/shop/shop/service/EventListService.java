package com.shop.shop.service;

import com.shop.shop.dto.EventListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventListService {

    public EventListDTO createEventList(EventListDTO eventListDTO, List<MultipartFile> files);
    public EventListDTO getEventListWithEventImages(Long eventListId);
    public Page<EventListDTO> getEventListPage(Pageable pageable);
    public Page<EventListDTO> getEventListPageWithDelFlag(Pageable pageable);
    public EventListDTO editEventList(Long eventListId, EventListDTO eventListDTO, List<MultipartFile> files);
    public void deleteEventList(Long eventListId);
    public void incrementViewCount(Long eventListId);

}

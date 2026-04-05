package com.shop.shop.service;

import com.shop.shop.domain.item.ItemImage;
import com.shop.shop.domain.list.EventImage;
import com.shop.shop.domain.list.EventList;
import com.shop.shop.dto.EventListDTO;
import com.shop.shop.dto.ItemDTO;
import com.shop.shop.repository.EventImageRepository;
import com.shop.shop.repository.EventListRepository;
import com.shop.shop.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventListServiceImpl implements EventListService {

    private final EventListRepository eventListRepository;
    private final EventImageRepository eventImageRepository;
    private final CustomFileUtil fileUtil;

    // 이벤트 리스트 등록
    @Override
    public EventListDTO createEventList(EventListDTO eventListDTO, List<MultipartFile> files) {
        EventList eventList = EventList.builder()
                .title(eventListDTO.getTitle())
                .writer(eventListDTO.getWriter())
                .content(eventListDTO.getContent())
                .date(LocalDateTime.now())
                .viewCount(0)
                .build();

        EventList savedEventList = eventListRepository.save(eventList);

        List<EventImage> images = null;

        // 이미지 저장
        if (files != null && !files.isEmpty()) {
            List<String> uploadFileNames = fileUtil.saveFiles(files);
            System.out.println("uploadFileNames: " + uploadFileNames);
            images = uploadFileNames.stream()
                    .map(fileName -> EventImage.builder()
                            .fileName(fileName)
                            .eventId(savedEventList.getId())
                            .build())
                    .toList();
            eventImageRepository.saveAll(images);
            return new EventListDTO(images, savedEventList);
        }

        return new EventListDTO(savedEventList);
    }

    // 특정 이벤트 리스트 조회
    @Override
    public EventListDTO getEventListWithEventImages(Long eventListId) {
        EventList eventList = eventListRepository.findByIdWithEventImages(eventListId);
        if (eventList == null) {
            throw new RuntimeException("해당 이벤트 페이지를 찾을 수 없습니다.");
        }
        List<EventImage> eventImageList = eventImageRepository.findAllByEventId(eventListId);
        return new EventListDTO(eventImageList, eventList);
    }

    // 이벤트 리스트 + 이미지 모두 조회(페이징) 삭제 포함
    @Override
    public Page<EventListDTO> getEventListPage(Pageable pageable) {
        Page<EventList> eventListPage = eventListRepository.findAllEventListPage(pageable);
        if (eventListPage == null || eventListPage.isEmpty()) {
            throw new RuntimeException("조회된 이벤트 리스트가 없습니다.");
        }
        Page<EventListDTO> eventListDTOPage = eventListPage.map(EventListDTO::new);
        return eventListDTOPage;
    }

    // 이벤트 리스트 + 이미지 모두 조회(페이징) 삭제 미포함
    @Override
    public Page<EventListDTO> getEventListPageWithDelFlag(Pageable pageable) {
        Page<EventList> eventListPage = eventListRepository.findAllEventListPageWithDelFlag(pageable);
        if (eventListPage == null || eventListPage.isEmpty()) {
            throw new RuntimeException("조회된 이벤트 리스트가 없습니다.");
        }
        Page<EventListDTO> eventListDTOPage = eventListPage.map(EventListDTO::new);
        return eventListDTOPage;
    }

    // 이벤트 리스트 수정
    @Override
    public EventListDTO editEventList(Long eventListId, EventListDTO eventListDTO, List<MultipartFile> files) {
        EventList eventList = eventListRepository.findById(eventListId).orElseThrow(() -> new RuntimeException("해당 이벤트 리스트를 찾을 수 없습니다."));
        eventList.changeTitle(eventListDTO.getTitle());
        eventList.changeWriter(eventListDTO.getWriter());
        eventList.changeContent(eventListDTO.getContent());
        eventList.changeDelFlag(eventListDTO.isDelFlag());
        eventList.clearList();

        EventList editEventList = eventListRepository.save(eventList);

        List<EventImage> images = null;

        // 이미지 저장
        if (files != null && !files.isEmpty()) {
            List<String> uploadFileNames = fileUtil.saveFiles(files);
            System.out.println("uploadFileNames: " + uploadFileNames);
            images = uploadFileNames.stream()
                    .map(fileName -> EventImage.builder()
                            .fileName(fileName)
                            .eventId(editEventList.getId())
                            .build())
                    .toList();
            eventImageRepository.saveAll(images);
            return new EventListDTO(images, eventList);
        }

        EventList editedEventList = eventListRepository.save(editEventList);
        return new EventListDTO(editedEventList);
    }

    // 특정 이벤트 리스트 삭제(논리적)
    @Override
    public void deleteEventList(Long eventListId) {
        EventList eventList = eventListRepository.findById(eventListId).orElseThrow(() -> new RuntimeException("해당 이벤트 리스트를 찾을 수 없습니다."));
        eventList.changeDelFlag(true);
        eventListRepository.save(eventList);
    }

    @Override
    public void incrementViewCount(Long eventListId) {
        EventList eventList = eventListRepository.findById(eventListId).orElseThrow(() -> new RuntimeException("해당 이벤트 리스트를 찾을 수 없습니다."));
        eventList.incrementViewCount();
        eventListRepository.save(eventList);
    }
}

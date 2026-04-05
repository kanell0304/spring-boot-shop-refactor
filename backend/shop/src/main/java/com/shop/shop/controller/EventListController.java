package com.shop.shop.controller;

import com.shop.shop.dto.EventListDTO;
import com.shop.shop.repository.EventListRepository;
import com.shop.shop.service.EventListService;
import com.shop.shop.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/eventList")
public class EventListController {

    private final EventListRepository eventListRepository;
    private final EventListService eventListService;
    private final CustomFileUtil fileUtil;

    // EventList 등록
    @PostMapping("/add")
    public ResponseEntity<EventListDTO> createEventList(
            @RequestParam("title") String title,
            @RequestParam("writer") String writer,
            @RequestParam("content") String content,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) {
        EventListDTO eventListDTO = new EventListDTO();
        eventListDTO.setTitle(title);
        eventListDTO.setWriter(writer);
        eventListDTO.setContent(content);

        // 파일 처리
        if (files != null && !files.isEmpty()) {
            List<String> uploadFileNames = fileUtil.saveFiles(files);
            eventListDTO.setUploadFileNames(uploadFileNames);
        }

        EventListDTO createdEventListDTO = eventListService.createEventList(eventListDTO, files);

        return ResponseEntity.ok(createdEventListDTO);
    }

    // 특정 이벤트 리스트 조회
    @GetMapping("/list/{eventListId}")
    public ResponseEntity<EventListDTO> getEventListByEventId(@PathVariable("eventListId") Long eventListId) {
        EventListDTO eventListDTO = eventListService.getEventListWithEventImages(eventListId);
        if (eventListDTO == null) {
            throw new RuntimeException("해당 이벤트 페이지를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(eventListDTO);
    }

    // 모든 EventList 조회(페이징) 삭제 포함
    @GetMapping("/listPageWithDelFlag")
    public ResponseEntity<Page<EventListDTO>> getEventListPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EventListDTO> eventListDTOPage = eventListService.getEventListPage(pageable);
        if (eventListDTOPage == null || eventListDTOPage.isEmpty()) {
            throw new RuntimeException("조회된 이벤트 페이지가 없습니다.");
        }
        return ResponseEntity.ok(eventListDTOPage);
    }

    // 모든 EventList 조회(페이징) 삭제 미포함
    @GetMapping("/listPage")
    public ResponseEntity<Page<EventListDTO>> getEventListPageWithDelFlag(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EventListDTO> eventListDTOPage = eventListService.getEventListPageWithDelFlag(pageable);
        if (eventListDTOPage == null || eventListDTOPage.isEmpty()) {
            throw new RuntimeException("조회된 이벤트 페이지가 없습니다.");
        }
        return ResponseEntity.ok(eventListDTOPage);
    }

    // 특정 이벤트 리스트 수정
    @PutMapping("/edit")
    public ResponseEntity<EventListDTO> createEventList(
            @RequestParam("eventListId") Long eventListId,
            @RequestParam("title") String title,
            @RequestParam("writer") String writer,
            @RequestParam("content") String content,
            @RequestParam("delFlag") boolean delFlag,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) {
        EventListDTO eventListDTO = new EventListDTO();
        eventListDTO.setTitle(title);
        eventListDTO.setWriter(writer);
        eventListDTO.setContent(content);
        eventListDTO.setDelFlag(delFlag);

        // 파일 처리
        if (files != null && !files.isEmpty()) {
            List<String> uploadFileNames = fileUtil.saveFiles(files);
            eventListDTO.setUploadFileNames(uploadFileNames);
        }

        EventListDTO editedEventListDTO = eventListService.editEventList(eventListId, eventListDTO, files);

        return ResponseEntity.ok(editedEventListDTO);
    }

    // 특정 이벤트 리스트 삭제(논리적)
    @DeleteMapping("/{eventListId}")
    public ResponseEntity<?> deleteEventList(@PathVariable("eventListId") Long eventListId) {
        try {
            eventListService.deleteEventList(eventListId);
            Map<String, String> response = Map.of("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = Map.of("result", "fail", "error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 특정 이벤트 리스트 조회수 증가
    @PutMapping("/incrementViewCount/{eventListId}")
    public ResponseEntity<?> incrementViewCount(@PathVariable("eventListId") Long eventListId) {
        try {
            eventListService.incrementViewCount(eventListId);
            Map<String, String> response = Map.of("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = Map.of("result", "fail", "error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}

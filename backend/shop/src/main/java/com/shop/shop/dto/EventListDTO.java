package com.shop.shop.dto;

import com.shop.shop.domain.item.ItemImage;
import com.shop.shop.domain.list.EventImage;
import com.shop.shop.domain.list.EventList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventListDTO {

    private Long eventListId;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime date;
    private int viewCount;
    private boolean delFlag;
    private List<String> uploadFileNames = new ArrayList<>();

    // 대표 이미지 1개를 포함하는 생성자
    public EventListDTO(EventList eventList, List<EventImage> images) {
        this.eventListId = eventList.getId();
        this.title = eventList.getTitle();
        this.writer = eventList.getWriter();
        this.content = eventList.getContent();
        this.date = eventList.getDate();
        this.viewCount = eventList.getViewCount();
        this.delFlag = eventList.isDelFlag();

        // 대표 이미지 설정
        this.uploadFileNames = (images != null && !images.isEmpty())
                ? List.of(images.get(0).getFileName())
                : List.of("default.png");
    }

    // DTO 변환 생성자
    public EventListDTO(EventList eventList) {
        this.eventListId = eventList.getId();
        this.title = eventList.getTitle();
        this.writer = eventList.getWriter();
        this.content = eventList.getContent();
        this.date = eventList.getDate();
        this.viewCount = eventList.getViewCount();
        this.delFlag = eventList.isDelFlag();

        // 파일 이름 리스트 변환
        this.uploadFileNames = (eventList.getImages() != null && !eventList.getImages().isEmpty())
                ? eventList.getImages().stream().map(EventImage::getFileName).toList()
                : new ArrayList<>(); // 빈 리스트로 처리
    }
    
    // 이미지 리스트를 포함하는 생성자
    public EventListDTO(List<EventImage> images, EventList eventList) {
        this.eventListId = eventList.getId();
        this.title = eventList.getTitle();
        this.writer = eventList.getWriter();
        this.content = eventList.getContent();
        this.date = eventList.getDate();
        this.viewCount = eventList.getViewCount();
        this.delFlag = eventList.isDelFlag();

        // 파일 이름 리스트 변환
        this.uploadFileNames = (images != null && !images.isEmpty())
            ? images.stream().map(EventImage::getFileName).toList()
                : new ArrayList<>(); // 빈 리스트로 처리
    }


}

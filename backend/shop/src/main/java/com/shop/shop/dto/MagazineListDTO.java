package com.shop.shop.dto;

import com.shop.shop.domain.list.EventImage;
import com.shop.shop.domain.list.EventList;
import com.shop.shop.domain.list.MagazineImage;
import com.shop.shop.domain.list.MagazineList;
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
public class MagazineListDTO {

    private Long magazineListId;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime date;
    private int viewCount;
    private boolean delFlag;
    private List<String> uploadFileNames = new ArrayList<>();

    // 대표 이미지 1개를 포함하는 생성자
    public MagazineListDTO(MagazineList magazineList, List<MagazineImage> images) {
        this.magazineListId = magazineList.getId();
        this.title = magazineList.getTitle();
        this.writer = magazineList.getWriter();
        this.content = magazineList.getContent();
        this.date = magazineList.getDate();
        this.viewCount = magazineList.getViewCount();
        this.delFlag = magazineList.isDelFlag();

        // 대표 이미지 설정
        this.uploadFileNames = (images != null && !images.isEmpty())
                ? List.of(images.get(0).getFileName())
                : List.of("default.png");
    }

    // DTO 변환 생성자
    public MagazineListDTO(MagazineList magazineList) {
        this.magazineListId = magazineList.getId();
        this.title = magazineList.getTitle();
        this.writer = magazineList.getWriter();
        this.content = magazineList.getContent();
        this.date = magazineList.getDate();
        this.viewCount = magazineList.getViewCount();
        this.delFlag = magazineList.isDelFlag();

        // 파일 이름 리스트 변환
        this.uploadFileNames = (magazineList.getImages() != null && !magazineList.getImages().isEmpty())
                ? magazineList.getImages().stream().map(MagazineImage::getFileName).toList()
                : new ArrayList<>(); // 빈 리스트로 처리
    }

    // 이미지 리스트를 포함하는 생성자
    public MagazineListDTO(List<MagazineImage> images, MagazineList magazineList) {
        this.magazineListId = magazineList.getId();
        this.title = magazineList.getTitle();
        this.writer = magazineList.getWriter();
        this.content = magazineList.getContent();
        this.date = magazineList.getDate();
        this.viewCount = magazineList.getViewCount();
        this.delFlag = magazineList.isDelFlag();

        // 파일 이름 리스트 변환
        this.uploadFileNames = (images != null && !images.isEmpty())
            ? images.stream().map(MagazineImage::getFileName).toList()
                : new ArrayList<>(); // 빈 리스트로 처리
    }


}

package com.shop.shop.dto;

import com.shop.shop.domain.list.ReviewImage;
import com.shop.shop.domain.list.ReviewList;
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
public class ReviewListDTO {

    private Long memberId;
    private String memberName;
    private Long itemId;
    private Long reviewId;
    private String title;
    private String writer;
    private String content;
    private int score;
    private LocalDateTime date;
    private boolean delFlag;

    private List<String> uploadFileNames = new ArrayList<>();

    // 대표 이미지 1개를 포함하는 생성자
    public ReviewListDTO(ReviewList reviewList, List<ReviewImage> images) {
        this.memberId = reviewList.getMember().getId();
        this.memberName = reviewList.getMember().getMemberName();
        this.itemId = reviewList.getItem().getId();
        this.reviewId = reviewList.getId();
        this.title = reviewList.getTitle();
        this.writer = reviewList.getWriter();
        this.content = reviewList.getContent();
        this.date = reviewList.getDate();
        this.score = reviewList.getScore();
        this.delFlag = reviewList.isDelFlag();

        // 대표 이미지 설정
        this.uploadFileNames = (images != null && !images.isEmpty())
                ? List.of(images.get(0).getFileName())
                : List.of("default.png");
    }

    // DTO 변환 생성자
    public ReviewListDTO(ReviewList reviewList) {
        this.memberId = reviewList.getMember().getId();
        this.memberName = reviewList.getMember().getMemberName();
        this.itemId = reviewList.getItem().getId();
        this.reviewId = reviewList.getId();
        this.title = reviewList.getTitle();
        this.writer = reviewList.getWriter();
        this.content = reviewList.getContent();
        this.date = reviewList.getDate();
        this.score = reviewList.getScore();
        this.delFlag = reviewList.isDelFlag();

        // 파일 이름 리스트 변환
        this.uploadFileNames = (reviewList.getImages() != null && !reviewList.getImages().isEmpty())
                ? reviewList.getImages().stream().map(ReviewImage::getFileName).toList()
                : new ArrayList<>(); // 빈 리스트로 처리
    }

    // 이미지 리스트를 포함하는 생성자
    public ReviewListDTO(List<ReviewImage> images, ReviewList reviewList) {
        this.memberId = reviewList.getMember().getId();
        this.memberName = reviewList.getMember().getMemberName();
        this.itemId = reviewList.getItem().getId();
        this.reviewId = reviewList.getId();
        this.title = reviewList.getTitle();
        this.writer = reviewList.getWriter();
        this.content = reviewList.getContent();
        this.date = reviewList.getDate();
        this.score = reviewList.getScore();
        this.delFlag = reviewList.isDelFlag();

        // 파일 이름 리스트 변환
        this.uploadFileNames = (images != null && !images.isEmpty())
                ? images.stream().map(ReviewImage::getFileName).toList()
                : new ArrayList<>(); // 빈 리스트로 처리
    }

}

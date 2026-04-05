package com.shop.shop.domain.list;

import com.shop.shop.domain.item.Item;
import com.shop.shop.domain.item.ItemImage;
import com.shop.shop.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewList {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_list_id")
    private Long id;

    private String title;
    private String writer;

    @Lob
    private String content;

    private int score;
    private LocalDateTime date;
    private boolean delFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 아이템 이미지
    // 저장, 병합(수정), 삭제
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "review_id")
    @Builder.Default
    private List<ReviewImage> images = new ArrayList<>();

    // 이미지 추가
    public void addImage(ReviewImage image) {
        image.setOrd(this.images.size());
        this.images.add(image);
    }

    // 이미지 파일명 추가
    public void addImageString(String fileName) {
        ReviewImage reviewImage = ReviewImage.builder()
                .fileName(fileName)
                .build();
        addImage(reviewImage);
    }

    // 이미지 삭제
    public void clearList() {
        this.images.clear();
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeWriter(String writer) {
        this.writer = writer;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeScore(int score) {
        this.score = score;
    }

    public void changeDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }

}

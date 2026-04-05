package com.shop.shop.domain.list;

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
public class MagazineList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "magazine_list_id")
    private Long id;

    private String title;
    private String writer;

    @Lob
    private String content;

    private LocalDateTime date;
    private int viewCount;

    // image 외래키
    // OneToMany - 게시글 하나에 여러 이미지 가능
    // orphanRemoval = true - 게시글 삭제시 해당 게시글에 속해있는 이미지도 자동으로 삭제
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "magazine_id")
    @Builder.Default
    private List<MagazineImage> images = new ArrayList<>();

    // 이미지 추가
    public void addImage(MagazineImage image) {
        image.setOrd(this.images.size());
        this.images.add(image);
    }

    // 이미지 파일명 추가
    public void addImageString(String fileName) {
        MagazineImage magazineImage = MagazineImage.builder()
                .fileName(fileName)
                .build();
        addImage(magazineImage);
    }

    // 이미지 삭제
    public void clearList() {
        this.images.clear();
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

}

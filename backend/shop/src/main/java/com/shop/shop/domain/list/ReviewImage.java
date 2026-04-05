package com.shop.shop.domain.list;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id")
    private Long id;

    private String fileName;

    @Setter
    private int ord;

    @Column(name = "review_id")
    private Long reviewId;

}

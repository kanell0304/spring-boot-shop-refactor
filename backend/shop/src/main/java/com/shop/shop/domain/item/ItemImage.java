package com.shop.shop.domain.item;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Item_Image")
public class ItemImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_image_id")
    private Long id;
    private String fileName;

    @Setter
    private int ord; // 그림 순번

    @Column(name = "item_id")
    private Long itemId;

}

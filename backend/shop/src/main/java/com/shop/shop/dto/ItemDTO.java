package com.shop.shop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.shop.domain.item.Item;
import com.shop.shop.domain.item.ItemImage;
import com.shop.shop.domain.item.ItemInfo;
import com.shop.shop.domain.item.ItemOption;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {

    private Long id;
    private String name;

    @Lob
    private String description;

    private int price;
    private float totalScore;
    private int discountRate;
    private boolean delFlag;
    private LocalDateTime dueDate;
    private int salesVolume;
    private Long categoryId;

    @Builder.Default
    private List<ItemOptionDTO> options = new ArrayList<>();

    @Builder.Default
    private Map<String, String> info = new HashMap<>();

    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();

    // 대표 이미지 1개만 가져오는 생성자
    public ItemDTO(Item item, List<ItemImage> images) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.totalScore = item.getTotalScore();
        this.discountRate = item.getDiscountRate();
        this.delFlag = item.isDelFlag();
        this.dueDate = item.getDueDate();
        this.salesVolume = item.getSalesVolume();
        this.categoryId = item.getCategoryId();

        // 대표 이미지 설정
        this.uploadFileNames = (images != null && !images.isEmpty())
                ? List.of(images.get(0).getFileName())
                : List.of("default.png");
    }

    // 대표 이미지 1개와 옵션을 가져오는 생성자
    public ItemDTO(Item item, List<ItemImage> images, List<ItemOption> options) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.totalScore = item.getTotalScore();
        this.discountRate = item.getDiscountRate();
        this.delFlag = item.isDelFlag();
        this.dueDate = item.getDueDate();
        this.salesVolume = item.getSalesVolume();
        this.categoryId = item.getCategoryId();

        // 대표 이미지 설정
        this.uploadFileNames = (images != null && !images.isEmpty())
                ? List.of(images.get(0).getFileName())
                : List.of("default.png");

        // 옵션 변환
        this.options = (options != null && !options.isEmpty())
                ? options.stream()
                .map(option -> new ItemOptionDTO(
                        option.getId(),
                        option.getOptionName(),
                        option.getOptionValue(),
                        option.getOptionPrice(),
                        option.getStockQty()
                ))
                .toList()
                : new ArrayList<>();
    }

    // 전체 정보 다 가져오는 생성자
    public ItemDTO(Item item, List<ItemImage> images, List<ItemOption> options, List<ItemInfo> infoList) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.totalScore = item.getTotalScore();
        this.discountRate = item.getDiscountRate();
        this.delFlag = item.isDelFlag();
        this.dueDate = item.getDueDate();
        this.salesVolume = item.getSalesVolume();
        this.categoryId = item.getCategoryId();

        // 옵션 변환
        this.options = (options != null && !options.isEmpty())
                ? options.stream()
                .map(option -> new ItemOptionDTO(
                        option.getId(),
                        option.getOptionName(),
                        option.getOptionValue(),
                        option.getOptionPrice(),
                        option.getStockQty()
                ))
                .toList()
                : new ArrayList<>();

//        // 대표 가격 설정 (가장 낮은 가격을 대표 가격으로 설정)
//        this.price = options.stream()
//                .mapToInt(ItemOption::getOptionPrice)
//                .min()
//                .orElse(0); // 옵션이 없을 경우 기본값 0

        // `infoList`가 null 이 아닐 때만 변환하여 `Map<String, String>` 형태로 저장
        this.info = (infoList != null && !infoList.isEmpty())
                ? infoList.stream()
                .collect(Collectors.toMap(
                        ItemInfo::getInfoKey,
                        ItemInfo::getInfoValue
                ))
                : new HashMap<>();

        // 파일 이름 리스트 변환
        this.uploadFileNames = (images != null && !images.isEmpty())
                ? images.stream().map(ItemImage::getFileName).toList()
                : new ArrayList<>(); // ✅ 빈 리스트로 처리
    }
}

package com.shop.shop.service;

import com.shop.shop.domain.cart.WishList;
import com.shop.shop.domain.category.CategoryItem;
import com.shop.shop.domain.item.Item;
import com.shop.shop.domain.item.ItemImage;
import com.shop.shop.domain.item.ItemInfo;
import com.shop.shop.domain.item.ItemOption;
import com.shop.shop.domain.member.Member;
import com.shop.shop.dto.ItemDTO;
import com.shop.shop.dto.ItemOptionDTO;
import com.shop.shop.dto.WishListDTO;
import com.shop.shop.repository.*;
import com.shop.shop.util.CustomFileUtil;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
//@Getter
@RequiredArgsConstructor
@Log4j2
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final CustomFileUtil fileUtil;
    private final WishListRepository wishListRepository;
    private final MemberRepository memberRepository;
    private final CategoryItemRepository categoryItemRepository;
    private final CategoryItemService categoryItemService;

    @Getter
    private Item savedItem;

    // 아이템 등록
//    @Transactional
    @Override
    public ItemDTO createItem(ItemDTO itemDTO, List<MultipartFile> files, Long categoryId) {
        Item item = Item.builder()
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .totalScore(0)
                .price(itemDTO.getPrice())
                .discountRate(itemDTO.getDiscountRate())
                .delFlag(false)
                .dueDate(LocalDateTime.now())
                .salesVolume(0)
                .categoryId(categoryId)
                .build();

        // 아이템 저장
        savedItem = itemRepository.save(item);
        log.info("옵션: " + itemDTO.getOptions().get(0).getOptionName());
        log.info("옵션: " + itemDTO.getOptions().get(0).getOptionValue());
        log.info("옵션: " + itemDTO.getOptions().get(0).getOptionPrice());

        List<ItemOption> savedOptions = new ArrayList<>();
        // 옵션 저장
        if (itemDTO.getOptions() != null) {
            List<ItemOption> options = itemDTO.getOptions().stream()
                    .map(optionDTO -> ItemOption.builder()
                            .optionName(optionDTO.getOptionName())
                            .optionValue(optionDTO.getOptionValue())
                            .optionPrice(optionDTO.getOptionPrice())
                            .stockQty(optionDTO.getStockQty())
                            .itemId(item.getId()) // 연관된 itemId 설정
                            .build())
                    .toList();
            savedOptions = itemOptionRepository.saveAll(options);
        }

        // 인포 저장
        if (itemDTO.getInfo() != null) {
            List<ItemInfo> infoList = itemDTO.getInfo().entrySet().stream()
                    .map(entry -> new ItemInfo(entry.getKey(), entry.getValue()))
                    .toList();
            item.getInfo().addAll(infoList);
        }

        List<ItemImage> images = null;

        // 이미지 저장
        if (files != null && !files.isEmpty()) {
            List<String> uploadFileNames = fileUtil.saveFiles(files);
            System.out.println("uploadFileNames: " + uploadFileNames);
            images = uploadFileNames.stream()
                    .map(fileName -> ItemImage.builder()
                            .fileName(fileName)
                            .itemId(item.getId()) // 연관된 Item ID 설정
                            .build())
                    .toList();
            itemImageRepository.saveAll(images);
            return new ItemDTO(item, images, savedOptions, item.getInfo());
        }
        return new ItemDTO(item, item.getImages(), savedOptions, item.getInfo());
    }

    // 페이징 조회 - 목록+이미지
    @Override
    public Page<ItemDTO> getAllItems(Pageable pageable) {
        Page<Item> itemPage = itemRepository.findAllWithImages(pageable);

        return itemPage.map(item -> {
            List<ItemImage> images = item.getImages();
            ItemImage representativeImage = (images != null && !images.isEmpty())
                    ? images.get(0)
                    : ItemImage.builder().fileName("default.png").build();

            return new ItemDTO(item, List.of(representativeImage));
        });
    }

    // 1개 데이터 조회 - 아이템+이미지+인포+옵션
    @Override
    public ItemDTO getOne(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. ID: " + id));

        // 개별적으로 연관 데이터를 가져옴
        List<ItemImage> images = itemImageRepository.findByItemId(id);
        List<ItemOption> options = itemOptionRepository.findByItemId(id);
        return new ItemDTO(item, images, options, item.getInfo());
    }

    // 이미지만 조회
    @Override
    public ResponseEntity<Resource> getImageUrlByFileName(String fileName) {
        Optional<ItemImage> image = itemImageRepository.findByFileName(fileName);
        if (image.isEmpty()) {
            return null;
        }
        return fileUtil.getFile(fileName);
    }

    // 모든 상품 조회(상품 + 이미지 + 옵션)
    @Override
    public Page<ItemDTO> getAllItemsWithImageAndOptionsAndInfo(Pageable pageable) {
        Page<Item> itemPage = itemRepository.findAllWithImages(pageable);

        List<Item> items = itemPage.getContent();
        List<Long> itemIds = items.stream().map(Item::getId).toList();

        // 옵션, 인포 한번에 조회
        List<ItemOption> options = itemOptionRepository.findByItemIds(itemIds);
        List<Object[]> rawInfo = itemRepository.findInfoByItemIds(itemIds);

        // 인포 groupBy
        Map<Long, List<ItemInfo>> infoMap = rawInfo.stream()
                .collect(Collectors.groupingBy(
                        row -> (Long) row[0],
                        Collectors.mapping(
                                row -> new ItemInfo((String) row[1], (String) row[2]),
                                Collectors.toList()
                        )
                ));

        Map<Long, List<ItemOption>> optionMap = options.stream()
                .collect(Collectors.groupingBy(
                        itemOption -> itemOption.getItemId(), // key 추출
                        Collectors.toList()                   // value 리스트화
                ));

// DTO 조립
        List<ItemDTO> itemDTOs = items.stream()
                .map(item -> new ItemDTO(
                        item,
                        item.getImages(),                            // images
                        optionMap.getOrDefault(item.getId(), List.of()), // options
                        infoMap.getOrDefault(item.getId(), List.of())    // info
                ))
                .toList();

        return new PageImpl<>(itemDTOs, pageable, itemPage.getTotalElements());
    }

    // 특정 아이템 옵션 조회
    @Override
    public List<ItemOptionDTO> getItemOptionByItemId(Long itemId) {
        List<ItemOption> itemOptionList = itemOptionRepository.findByItemId(itemId);
        if (itemOptionList == null || itemOptionList.isEmpty()) {
            throw new RuntimeException("해당 아이템에 대한 옵션이 조회되지 않습니다.");
        }
        List<ItemOptionDTO> itemOptionDTOList = itemOptionList.stream().map(ItemOptionDTO::new).toList();
        return itemOptionDTOList;
    }

    // 아이템 수정
    @Override
    public ItemDTO updateItem(Long id, ItemDTO itemDTO, List<MultipartFile> files) {
        Item item = itemRepository.findById(id).orElseThrow();
        CategoryItem categoryItem = categoryItemRepository.findByItemId(itemDTO.getId());
        if (categoryItem == null) {
            throw new RuntimeException("해당 상품이 속한 카테고리를 찾지 못했습니다.");
        }
        if (item.getCategoryId() != itemDTO.getCategoryId()) {
            categoryItemRepository.deleteById(categoryItem.getId());
            categoryItemService.registerCategoryItem(item, itemDTO.getCategoryId());
        }


        // 기본 필드 수정
        if (itemDTO.getName() != null) {
            item.changeName(itemDTO.getName());
        }
        if (itemDTO.getDescription() != null) {
            item.changeDescription(itemDTO.getDescription());
        }
        if (itemDTO.getCategoryId() != null) {
            item.changeCategoryId(itemDTO.getCategoryId());
        }
        item.changeDiscountRate(itemDTO.getDiscountRate());
        item.changeDelFlag(itemDTO.isDelFlag());

        // 인포 수정
        item.getInfo().clear();
        if (itemDTO.getInfo() != null) {
            itemDTO.getInfo().forEach((key, value) -> item.addInfo(new ItemInfo(key, value)));
        }

        // 옵션 수정
        item.getOptions().clear();
        if (itemDTO.getOptions() != null) {
            List<ItemOption> updatedOptions = itemDTO.getOptions().stream()
                    .map(optionDTO -> ItemOption.builder()
                            .optionName(optionDTO.getOptionName())
                            .optionValue(optionDTO.getOptionValue())
                            .optionPrice(optionDTO.getOptionPrice())
                            .stockQty(optionDTO.getStockQty())
                            .itemId(item.getId())
                            .build())
                    .toList();
            item.getOptions().addAll(updatedOptions);
        }

        // 기존 이미지 제거
        item.clearList();

        // 유지할 이미지 파일명 등록 (프론트에서 받은 uploadFileNames 기준)
        if (itemDTO.getUploadFileNames() != null) {
            for (String fileName : itemDTO.getUploadFileNames()) {
                item.addImage(ItemImage.builder().fileName(fileName).build());
            }
        }

        // 새로 업로드된 이미지 추가
        if (files != null && !files.isEmpty()) {
            List<String> newFileNames = fileUtil.saveFiles(files);
            for (String fileName : newFileNames) {
                item.addImage(ItemImage.builder().fileName(fileName).build());
            }
            System.out.println("[업데이트] 추가된 파일 수: " + newFileNames.size());
        } else {
            System.out.println("[업데이트] 추가된 파일 없음");
        }

        itemRepository.save(item);

        return new ItemDTO(item, item.getImages(), item.getOptions(), item.getInfo());
    }


    // 논리적 삭제
    @Override
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow();
        item.changeDelFlag(true);
        itemRepository.save(item);
    }

}

package com.shop.shop.service;

import com.shop.shop.dto.ReviewListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewListService {

    public ReviewListDTO createReviewList(ReviewListDTO reviewListDTO, List<MultipartFile> files);
    public boolean checkPurchaseStatus(Long memberId, Long itemId);
    public ReviewListDTO getReviewListWithReviewImages(Long reviewListId);
    public Page<ReviewListDTO> getReviewListPage(Pageable pageable);
    public Page<ReviewListDTO> getReviewListPageWithDelFlag(Pageable pageable);
    public Page<ReviewListDTO> getReviewListPageByItemId(Long itemId, Pageable pageable);
    public Page<ReviewListDTO> getReviewListPageByItemIdWithDelFlag(Long itemId, Pageable pageable);
    public ReviewListDTO editReviewList(ReviewListDTO reviewListDTO, List<MultipartFile> files);
    public void deleteReviewList(ReviewListDTO reviewListDTO);

}

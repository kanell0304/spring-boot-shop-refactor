package com.shop.shop.service;

import com.shop.shop.dto.MagazineListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MagazineListService {

    public MagazineListDTO createMagazineList(MagazineListDTO magazineListDTO, List<MultipartFile> files);
    public MagazineListDTO getMagazineListWithMagazineImages(Long magazineListId);
    public Page<MagazineListDTO> getMagazineListPage(Pageable pageable);
    public Page<MagazineListDTO> getMagazineListPageWithDelFlag(Pageable pageable);
    public MagazineListDTO editMagazineList(Long magazineListId, MagazineListDTO magazineListDTO, List<MultipartFile> files);
    public void deleteMagazineList(Long magazineListId);
    public void incrementViewCount(Long magazineListId);

}

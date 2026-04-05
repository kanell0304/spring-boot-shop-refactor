package com.shop.shop.service;

import com.shop.shop.domain.list.MagazineImage;
import com.shop.shop.domain.list.MagazineList;
import com.shop.shop.dto.MagazineListDTO;
import com.shop.shop.repository.MagazineImageRepository;
import com.shop.shop.repository.MagazineListRepository;
import com.shop.shop.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MagazineListServiceImpl implements MagazineListService {

    private final MagazineListRepository magazineListRepository;
    private final MagazineImageRepository magazineImageRepository;
    private final CustomFileUtil fileUtil;

    // 매거진 리스트 등록
    @Override
    public MagazineListDTO createMagazineList(MagazineListDTO magazineListDTO, List<MultipartFile> files) {
        MagazineList magazineList = MagazineList.builder()
                .title(magazineListDTO.getTitle())
                .writer(magazineListDTO.getWriter())
                .content(magazineListDTO.getContent())
                .date(LocalDateTime.now())
                .viewCount(0)
                .build();

        MagazineList savedMagazineList = magazineListRepository.save(magazineList);

        List<MagazineImage> images = null;

        // 이미지 저장
        if (files != null && !files.isEmpty()) {
            List<String> uploadFileNames = fileUtil.saveFiles(files);
            System.out.println("uploadFileNames: " + uploadFileNames);
            images = uploadFileNames.stream()
                    .map(fileName -> MagazineImage.builder()
                            .fileName(fileName)
                            .magazineId(savedMagazineList.getId())
                            .build())
                    .toList();
            magazineImageRepository.saveAll(images);
            return new MagazineListDTO(images, savedMagazineList);
        }

        return new MagazineListDTO(savedMagazineList);
    }

    // 특정 매거진 리스트 조회
    @Override
    public MagazineListDTO getMagazineListWithMagazineImages(Long magazineListId) {
        MagazineList magazineList = magazineListRepository.findByIdWithMagazineImages(magazineListId);
        if (magazineList == null) {
            throw new RuntimeException("해당 매거진 페이지를 찾을 수 없습니다.");
        }
        List<MagazineImage> magazineImageList = magazineImageRepository.findAllByMagazineId(magazineListId);
        return new MagazineListDTO(magazineImageList, magazineList);
    }

    // 매거진 리스트 + 이미지 모두 조회(페이징) 삭제 포함
    @Override
    public Page<MagazineListDTO> getMagazineListPage(Pageable pageable) {
        Page<MagazineList> magazineListPage = magazineListRepository.findAllMagazineListPage(pageable);
        if (magazineListPage == null || magazineListPage.isEmpty()) {
            throw new RuntimeException("조회된 매거진 리스트가 없습니다.");
        }
        Page<MagazineListDTO> magazineListDTOPage = magazineListPage.map(MagazineListDTO::new);
        return magazineListDTOPage;
    }

    // 매거진 리스트 + 이미지 모두 조회(페이징) 삭제 미포함
    @Override
    public Page<MagazineListDTO> getMagazineListPageWithDelFlag(Pageable pageable) {
        Page<MagazineList> magazineListPage = magazineListRepository.findAllMagazineListPageWithDelFlag(pageable);
        if (magazineListPage == null || magazineListPage.isEmpty()) {
            throw new RuntimeException("조회된 매거진 리스트가 없습니다.");
        }
        Page<MagazineListDTO> magazineListDTOPage = magazineListPage.map(MagazineListDTO::new);
        return magazineListDTOPage;
    }

    // 매거진 리스트 수정
    @Override
    public MagazineListDTO editMagazineList(Long magazineListId, MagazineListDTO magazineListDTO, List<MultipartFile> files) {
        MagazineList magazineList = magazineListRepository.findById(magazineListId).orElseThrow(() -> new RuntimeException("해당 매거진 리스트를 찾을 수 없습니다."));
        magazineList.changeTitle(magazineListDTO.getTitle());
        magazineList.changeWriter(magazineListDTO.getWriter());
        magazineList.changeContent(magazineListDTO.getContent());
        magazineList.changeDelFlag(magazineListDTO.isDelFlag());
        magazineList.clearList();

        MagazineList editMagazineList = magazineListRepository.save(magazineList);

        List<MagazineImage> images = null;

        // 이미지 저장
        if (files != null && !files.isEmpty()) {
            List<String> uploadFileNames = fileUtil.saveFiles(files);
            System.out.println("uploadFileNames: " + uploadFileNames);
            images = uploadFileNames.stream()
                    .map(fileName -> MagazineImage.builder()
                            .fileName(fileName)
                            .magazineId(editMagazineList.getId())
                            .build())
                    .toList();
            magazineImageRepository.saveAll(images);
            return new MagazineListDTO(images, magazineList);
        }

        MagazineList editedMagazineList = magazineListRepository.save(editMagazineList);
        return new MagazineListDTO(editedMagazineList);
    }

    // 특정 매거진 리스트 삭제(논리적)
    @Override
    public void deleteMagazineList(Long magazineListId) {
        MagazineList magazineList = magazineListRepository.findById(magazineListId).orElseThrow(() -> new RuntimeException("해당 매거진 리스트를 찾을 수 없습니다."));
        magazineList.changeDelFlag(true);
        magazineListRepository.save(magazineList);
    }

    @Override
    public void incrementViewCount(Long magazineListId) {
        MagazineList magazineList = magazineListRepository.findById(magazineListId).orElseThrow(() -> new RuntimeException("해당 매거진 리스트를 찾을 수 없습니다."));
        magazineList.incrementViewCount();
        magazineListRepository.save(magazineList);
    }
}

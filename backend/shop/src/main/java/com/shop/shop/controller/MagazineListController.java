package com.shop.shop.controller;

import com.shop.shop.dto.MagazineListDTO;
import com.shop.shop.repository.MagazineListRepository;
import com.shop.shop.service.MagazineListService;
import com.shop.shop.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/magazineList")
public class MagazineListController {

    private final MagazineListRepository magazineListRepository;
    private final MagazineListService magazineListService;
    private final CustomFileUtil fileUtil;

    // 매거진 리스트 등록
    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<MagazineListDTO> createMagazineList(
            @RequestParam("title") String title,
            @RequestParam("writer") String writer,
            @RequestParam("content") String content,
//            @RequestParam("content") MultipartFile content,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
//        String contentFile = new String(content.getBytes(), StandardCharsets.UTF_8);

        MagazineListDTO magazineListDTO = new MagazineListDTO();
        magazineListDTO.setTitle(title);
        magazineListDTO.setWriter(writer);
        magazineListDTO.setContent(content);

        // 파일 처리
        if (files != null && !files.isEmpty()) {
            List<String> uploadFileNames = fileUtil.saveFiles(files);
            magazineListDTO.setUploadFileNames(uploadFileNames);
        }

        MagazineListDTO createdMagazineListDTO = magazineListService.createMagazineList(magazineListDTO, files);

        return ResponseEntity.ok(createdMagazineListDTO);
    }

    // 특정 매거진 리스트 조회
    @GetMapping("/list/{magazineListId}")
    public ResponseEntity<MagazineListDTO> getMagazineListByMagazineId(@PathVariable("magazineListId") Long magazineListId) {
        MagazineListDTO magazineListDTO = magazineListService.getMagazineListWithMagazineImages(magazineListId);
        if (magazineListDTO == null) {
            throw new RuntimeException("해당 매거진 페이지를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(magazineListDTO);
    }

    // 모든 매거진 리스트 조회(페이징) 삭제 포함
    @GetMapping("/listPageWithDelFlag")
    public ResponseEntity<Page<MagazineListDTO>> getMagazineListPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MagazineListDTO> magazineListDTOPage = magazineListService.getMagazineListPage(pageable);
        if (magazineListDTOPage == null || magazineListDTOPage.isEmpty()) {
            throw new RuntimeException("조회된 매거진 페이지가 없습니다.");
        }
        return ResponseEntity.ok(magazineListDTOPage);
    }

    // 모든 매거진 리스트 조회(페이징) 삭제 미포함
    @GetMapping("/listPage")
    public ResponseEntity<Page<MagazineListDTO>> getMagazineListPageWithDelFlag(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MagazineListDTO> magazineListDTOPage = magazineListService.getMagazineListPageWithDelFlag(pageable);
        if (magazineListDTOPage == null || magazineListDTOPage.isEmpty()) {
            throw new RuntimeException("조회된 이벤트 페이지가 없습니다.");
        }
        return ResponseEntity.ok(magazineListDTOPage);
    }

    // 특정 매거진 리스트 수정
    @PutMapping("/edit")
    public ResponseEntity<MagazineListDTO> editMagazineList(
            @RequestParam("magazineListId") Long magazineListId,
            @RequestParam("title") String title,
            @RequestParam("writer") String writer,
            @RequestParam("content") String content,
            @RequestParam("delFlag") boolean delFlag,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) {
        MagazineListDTO MagazineListDTO = new MagazineListDTO();
        MagazineListDTO.setTitle(title);
        MagazineListDTO.setWriter(writer);
        MagazineListDTO.setContent(content);
        MagazineListDTO.setDelFlag(delFlag);

        // 파일 처리
        if (files != null && !files.isEmpty()) {
            List<String> uploadFileNames = fileUtil.saveFiles(files);
            MagazineListDTO.setUploadFileNames(uploadFileNames);
        }

        MagazineListDTO editedMagazineListDTO = magazineListService.editMagazineList(magazineListId, MagazineListDTO, files);

        return ResponseEntity.ok(editedMagazineListDTO);
    }

    // 특정 매거진 리스트 삭제(논리적)
    @DeleteMapping("/{magazineListId}")
    public ResponseEntity<?> deleteMagazineList(@PathVariable("magazineListId") Long magazineListId) {
        try {
            magazineListService.deleteMagazineList(magazineListId);
            Map<String, String> response = Map.of("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = Map.of("result", "fail", "error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 특정 매거진 리스트 조회수 증가
    @PutMapping("/incrementViewCount/{magazineListId}")
    public ResponseEntity<?> incrementViewCount(@PathVariable("magazineListId") Long magazineListId) {
        try {
            magazineListService.incrementViewCount(magazineListId);
            Map<String, String> response = Map.of("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = Map.of("result", "fail", "error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}

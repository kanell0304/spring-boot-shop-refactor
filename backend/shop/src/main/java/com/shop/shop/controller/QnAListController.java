package com.shop.shop.controller;

import com.shop.shop.dto.QnAListDTO;
import com.shop.shop.service.QnAListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/qnaList")
public class QnAListController {

    private final QnAListService qnAListService;

    // 질의응답 리스트 등록
    @PostMapping("/add")
    public ResponseEntity<QnAListDTO> createQnAList(@RequestBody QnAListDTO qnAListdto) {
        QnAListDTO savedQnAListDTO = qnAListService.createQnAList(qnAListdto);
        if (savedQnAListDTO == null) {
            throw new RuntimeException("질의응답 등록이 정상적으로 동작하지 않았습니다.");
        }
        return ResponseEntity.ok(savedQnAListDTO);
    }

    // 질의응답 리스트 모두 조회(페이징) 삭제 포함
    @GetMapping("/listPage")
    public ResponseEntity<Page<QnAListDTO>> getAllQnAListPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<QnAListDTO> qnAListDTOPage = qnAListService.getAllQnAListPage(pageable);
        if (qnAListDTOPage == null) {
            throw new RuntimeException("조회된 질의응답 페이지가 없습니다.");
        }
        return ResponseEntity.ok(qnAListDTOPage);
    }

    // 질의응답 리스트 모두 조회(페이징) 삭제 미포함
    @GetMapping("/listPageWithDelFlag")
    public ResponseEntity<Page<QnAListDTO>> getAllQnAListPageWithDelFlag(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<QnAListDTO> qnAListDTOPage = qnAListService.getAllQnAListPageWithDelFlag(pageable);
        if (qnAListDTOPage == null) {
            throw new RuntimeException("조회된 질의응답 페이지가 없습니다.");
        }
        return ResponseEntity.ok(qnAListDTOPage);
    }
    
    // 질의응답Id를 기준으로 조회 삭제 포함
    @GetMapping("/list/{qnaListId}")
    public ResponseEntity<QnAListDTO> getQnAListByQnAListId(@PathVariable("qnaListId") Long qnaListId) {
        QnAListDTO qnAListDTO = qnAListService.getQnAListByQnAListId(qnaListId);
        if (qnAListDTO == null) {
            throw new RuntimeException("조회된 질의응답 페이지가 없습니다.");
        }
        return ResponseEntity.ok(qnAListDTO);
    }

    // 질의응답Id를 기준으로 조회 삭제 미포함
    @GetMapping("/listWithDelFlag/{qnaListId}")
    public ResponseEntity<QnAListDTO> getQnAListByQnAListIdWithDelFlag(@PathVariable("qnaListId") Long qnaListId) {
        QnAListDTO qnAListDTO = qnAListService.getQnAListByQnAListIdWithDelFlag(qnaListId);
        if (qnAListDTO == null) {
            throw new RuntimeException("조회된 질의응답 페이지가 없습니다.");
        }
        return ResponseEntity.ok(qnAListDTO);
    }
    
    // 특정 질의응답 리스트 수정
    @PutMapping("/edit")
    public ResponseEntity<QnAListDTO> editQnAListByQnAListId(@RequestBody QnAListDTO qnAListDTO) {
        QnAListDTO editedQnAList = qnAListService.editQnAList(qnAListDTO);
        if (editedQnAList == null) {
            throw new RuntimeException("질의응답 수정이 정삭적으로 동작하지 않았습니다.");
        }
        return ResponseEntity.ok(editedQnAList);
    }

    // 특정 질의응답 리스트 삭제(논리적 삭제)
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteQnAListByQnAListId(@RequestBody QnAListDTO qnAListDTO) {
        try {
            qnAListService.deleteQnAList(qnAListDTO);
            Map<String, String> response = Map.of("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = Map.of("result", "fail", "error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}

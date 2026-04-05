package com.shop.shop.controller;

import com.shop.shop.dto.ScoreListDTO;
import com.shop.shop.service.ScoreListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/scoreList")
public class ScoreListController {

    private final ScoreListService scoreListService;

    // 점수 내역 생성 API
    @PostMapping("/add")
    public ResponseEntity<ScoreListDTO> createScore(@RequestBody ScoreListDTO scoreListDTO) {
        ScoreListDTO createdScore = scoreListService.createScore(scoreListDTO);
        if (createdScore == null) {
            throw new RuntimeException("점수 내역 생성이 정상적으로 동작하지 않았습니다.");
        }

        return ResponseEntity.ok(createdScore);
    }

    // 상품Id와 회원Id를 기준으로 점수 내역 조회
    @PostMapping("/getScoreByItemIdAndMemberId")
    public ResponseEntity<ScoreListDTO> getScore(@RequestBody ScoreListDTO scoreListDTO) {
        ScoreListDTO score = scoreListService.getScoreByItemIdAndMemberId(scoreListDTO.getItemId(), scoreListDTO.getMemberId());
        if (score == null) {
            throw new RuntimeException("점수 내역 생성이 정상적으로 동작하지 않았습니다.");
        }

        return ResponseEntity.ok(score);
    }

    // 상품Id를 기준으로 점수 내역 조회
    @PostMapping("/getScoreByItemId")
    public ResponseEntity<List<ScoreListDTO>> getScoreByItemId(@RequestBody ScoreListDTO scoreListDTO) {
        List<ScoreListDTO> createdScore = scoreListService.getScoreListByItemId(scoreListDTO.getItemId());
        if (createdScore == null) {
            throw new RuntimeException("점수 내역 생성이 정상적으로 동작하지 않았습니다.");
        }

        return ResponseEntity.ok(createdScore);
    }

    // 회원Id를 기준으로 점수 내역 조회
    @PostMapping("/getScoreByMemberId")
    public ResponseEntity<List<ScoreListDTO>> getScoreByMemberId(@RequestBody ScoreListDTO scoreListDTO) {
        List<ScoreListDTO> createdScore = scoreListService.getScoreListByMemberId(scoreListDTO.getMemberId());
        if (createdScore == null) {
            throw new RuntimeException("점수 내역 생성이 정상적으로 동작하지 않았습니다.");
        }

        return ResponseEntity.ok(createdScore);
    }

}

package com.shop.shop.controller;

import com.shop.shop.domain.category.Category;
import com.shop.shop.domain.member.Mileage;
import com.shop.shop.dto.MileageDTO;
import com.shop.shop.service.MileageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/mileage")
public class MileageController {

    private final MileageService mileageService;

    // 마일리지 내역 생성
    @PostMapping("/add")
    public ResponseEntity<MileageDTO> registerMileage(@RequestBody MileageDTO mileageDTO) {
        MileageDTO savedMileage = mileageService.createMileage(mileageDTO);
        if (savedMileage == null) {
            throw new RuntimeException("마일리지 생성에 실패했습니다.");
        }
        return ResponseEntity.ok(savedMileage);
    }

    // 모든 마일리지 내역 조회(페이징)
    @GetMapping("/listPage")
    public ResponseEntity<Page<MileageDTO>> getMileageWithPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MileageDTO> category = mileageService.findAllMileagePage(pageable);
        return ResponseEntity.ok(category);
    }

    // 특정 회원 마일리지 내역 모두 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<List<MileageDTO>> getMileageListByMember(@PathVariable Long memberId) {
        List<MileageDTO> savedMileageList = mileageService.findAllByMemberId(memberId);
        if (savedMileageList == null) {
            throw new RuntimeException("마일리지 조회에 실패하였습니다.");
        }
        return ResponseEntity.ok(savedMileageList);
    }

    // 특정 회원 마일리지 내역 모두 조회(페이징)
    @GetMapping("/page/{memberId}")
    public ResponseEntity<Page<MileageDTO>> getMileageListByMemberPage(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MileageDTO> savedMileageList = mileageService.findAllByMemberId(pageable, memberId);
        if (savedMileageList == null) {
            throw new RuntimeException("마일리지 조회에 실패하였습니다.");
        }
        return ResponseEntity.ok(savedMileageList);
    }

    // 특정 기간 마일리지 내역 모두 조회
    @GetMapping("/duringDate")
    public ResponseEntity<List<MileageDTO>> getMileageListDuringDate(@RequestBody MileageDTO mileageDTO) {
        List<MileageDTO> getMileageList = mileageService.findByDuringPeriod(mileageDTO.getStartDate(), mileageDTO.getEndDate());
        if (getMileageList == null || getMileageList.isEmpty()) {
            throw new RuntimeException("해당 기간에 조회된 마일리지 내역이 없습니다.");
        }
        return ResponseEntity.ok(getMileageList);
    }

    // 특정 기간 마일리지 내역 모두 조회(페이징)
    @GetMapping("/duringDatePage")
    public ResponseEntity<Page<MileageDTO>> getMileageListDuringDatePage(
            @RequestBody MileageDTO mileageDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MileageDTO> getMileageList = mileageService.findByDuringPeriod(pageable, mileageDTO.getStartDate(), mileageDTO.getEndDate());
        if (getMileageList == null || getMileageList.isEmpty()) {
            throw new RuntimeException("해당 기간에 조회된 마일리지 내역이 없습니다.");
        }
        return ResponseEntity.ok(getMileageList);
    }

    // 특정 회원의 특정 기간 마일리지 내역 모두 조회
    @GetMapping("/duringMember")
    public ResponseEntity<List<MileageDTO>> getMileageListByMemberAndDuringDate(@RequestBody MileageDTO mileageDTO) {
        List<MileageDTO> getMileageList = mileageService.findByDuringPeriodFromMemberId(mileageDTO.getMemberId(), mileageDTO.getStartDate(), mileageDTO.getEndDate());
        if (getMileageList == null || getMileageList.isEmpty()) {
            throw new RuntimeException("해당 회원 또는 해당 기간에 조회된 마일리지 내역이 없습니다.");
        }
        return ResponseEntity.ok(getMileageList);
    }

    // 특정 회원의 특정 기간 마일리지 내역 모두 조회(페이징)
    @GetMapping("/duringMemberPage")
    public ResponseEntity<Page<MileageDTO>> getMileageListByMemberAndDuringDatePage(
            @RequestBody MileageDTO mileageDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MileageDTO> getMileageList = mileageService.findByDuringPeriodFromMemberId(pageable, mileageDTO.getMemberId(), mileageDTO.getStartDate(), mileageDTO.getEndDate());
        if (getMileageList == null || getMileageList.isEmpty()) {
            throw new RuntimeException("해당 회원 또는 해당 기간에 조회된 마일리지 내역이 없습니다.");
        }
        return ResponseEntity.ok(getMileageList);
    }

    // 마일리지 내역 삭제
    @DeleteMapping("/{mileageId}")
    public ResponseEntity<?> deleteItemFromMileage(@PathVariable("mileageId") Long mileageId) {
        try {
            mileageService.deleteMileageById(mileageId);
            Map<String, String> response = Map.of("result", "success");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("result","fail","error",e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("result","fail","error",e.getMessage()));
        }
    }

}

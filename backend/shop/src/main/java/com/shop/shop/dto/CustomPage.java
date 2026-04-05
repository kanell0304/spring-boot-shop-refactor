package com.shop.shop.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class CustomPage<T> {

    private final List<T> content;          // 데이터 리스트
    private final int page;                 // 현재 페이지
    private final int size;                 // 페이지 크기
    private final int totalPages;           // 전체 페이지 수
    private final int totalElements;        // 전체 데이터 개수
    private final boolean isFirst;          // 첫 페이지 여부
    private final boolean isLast;           // 마지막 페이지 여부
    private final List<Integer> paginationRange; // 페이지네이션 범위
    private final boolean hasPrev;          // 이전 페이지 존재 여부
    private final boolean hasNext;          // 다음 페이지 존재 여부
    private final Integer prevPage;         // 이전 페이지 번호 (없으면 null)
    private final Integer nextPage;         // 다음 페이지 번호 (없으면 null)

    // 생성자
    private CustomPage(Page<T> page, List<Integer> paginationRange,
                       boolean hasPrev, boolean hasNext,
                       Integer prevPage, Integer nextPage) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = (int) page.getTotalElements();
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
        this.paginationRange = paginationRange;
        this.hasPrev = hasPrev;
        this.hasNext = hasNext;
        this.prevPage = prevPage;
        this.nextPage = nextPage;
    }

    // dto는 비지니스 로직 없음, but 포함 가능한 메서드
    // 1) 유틸리티 메서드 - 간단한 데이터 변환을 위한 기능 - (문자열 포매팅, 날짜 변환..)
    // 2) 정적 팩토리 메서드 : 엔티티->dto 변환하는 메서드, 객체 생성을 돕는 메서드
    public static <T> CustomPage<T> of(Page<T> page, int groupSize) {
        int currentPage = page.getNumber(); // 현재 페이지 = pageable.getPage();
        int totalPages = page.getTotalPages(); // 전체 페이지

        // 총 23페이지 - : 21~23페이지 범위 생성됨
        // 21페이지 : (21 / 10) * 10 = 20
        // startPage : 20 + 1
        // endPage : min ( 20 + 10 -1  vs totalPage : 23 ) 둘중 작은것 반환
        int currentGroup = (currentPage / groupSize) * groupSize;
        int startPage = currentGroup + 1;
        int endPage = Math.min(startPage + groupSize - 1, totalPages);

        boolean hasPrev = startPage > 1; // 이전 범위 있음
        boolean hasNext = endPage < totalPages; // 다음 범위 없음
        Integer prevPage = hasPrev ? startPage - 1 : null; // 21 - 1 : 20페이지 이동
        Integer nextPage = hasNext ? endPage + 1 : null; //

        List<Integer> range = IntStream.rangeClosed(startPage, endPage)
                .boxed() // int->interger 변환
                .collect(Collectors.toList()); // 리스트 변환

        return new CustomPage<>(page, range, hasPrev, hasNext, prevPage, nextPage);
    }
}

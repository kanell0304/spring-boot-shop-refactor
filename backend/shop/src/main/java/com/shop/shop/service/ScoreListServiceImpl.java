package com.shop.shop.service;

import com.shop.shop.domain.item.Item;
import com.shop.shop.domain.list.ScoreList;
import com.shop.shop.domain.member.Member;
import com.shop.shop.dto.ScoreListDTO;
import com.shop.shop.repository.ItemRepository;
import com.shop.shop.repository.MemberRepository;
import com.shop.shop.repository.ScoreListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ScoreListServiceImpl implements ScoreListService{

    private final ScoreListRepository scoreListRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    // 점수 내역 생성
    @Override
    public ScoreListDTO createScore(ScoreListDTO scoreListDTO) {
        Item item = itemRepository.findById(scoreListDTO.getItemId()).orElseThrow(() -> new RuntimeException("해당 상품을 찾을 수 없습니다."));
        Member member = memberRepository.findById(scoreListDTO.getMemberId()).orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        ScoreList scoreList = ScoreList.builder()
                .score(scoreListDTO.getScore())
                .item(item)
                .member(member)
                .build();
        ScoreList savedScore = scoreListRepository.save(scoreList);

        return new ScoreListDTO(savedScore);
    }

    // 상품Id와 회원Id를 기준으로 점수 조회
    @Override
    public ScoreListDTO getScoreByItemIdAndMemberId(Long itemId, Long memberId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("해당 상품을 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
        ScoreList scoreList = scoreListRepository.findAllScoreListByItemIdAndMemberId(item.getId(), member.getId());
        if (scoreList == null) {
            throw new RuntimeException("조회된 점수내역이 없습니다.");
        }
        return new ScoreListDTO(scoreList);
    }

    // 상품Id를 기준으로 점수 모두 조회
    @Override
    public List<ScoreListDTO> getScoreListByItemId(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("해당 상품을 찾을 수 없습니다."));

        List<ScoreList> scoreList = scoreListRepository.findAllScoreListByItemId(item.getId());
        if (scoreList == null || scoreList.isEmpty()) {
            throw new RuntimeException("조회된 점수내역이 없습니다.");
        }
        return scoreList.stream().map(ScoreListDTO::new).toList();
    }

    // 회원Id를 기준으로 점수 모두 조회
    @Override
    public List<ScoreListDTO> getScoreListByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        List<ScoreList> scoreList = scoreListRepository.findAllScoreListByMemberId(member.getId());
        if (scoreList == null || scoreList.isEmpty()) {
            throw new RuntimeException("조회된 점수내역이 없습니다.");
        }
        return scoreList.stream().map(ScoreListDTO::new).toList();
    }
}

package com.shop.shop.service;

import com.shop.shop.dto.ScoreListDTO;

import java.util.List;

public interface ScoreListService {

    public ScoreListDTO createScore(ScoreListDTO scoreListDTO);
    public ScoreListDTO getScoreByItemIdAndMemberId(Long itemId, Long memberId);
    public List<ScoreListDTO> getScoreListByItemId(Long itemId);
    public List<ScoreListDTO> getScoreListByMemberId(Long memberId);

}

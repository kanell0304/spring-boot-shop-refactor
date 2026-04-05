package com.shop.shop.dto;

import com.shop.shop.domain.list.ScoreList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreListDTO {

    private Long scoreId;
    private int score;
    private Long memberId;
    private Long itemId;

    public ScoreListDTO(ScoreList scoreList) {
        this.scoreId = scoreList.getId();
        this.score = scoreList.getScore();
        this.memberId = scoreList.getMember().getId();
        this.itemId = scoreList.getItem().getId();
    }

}

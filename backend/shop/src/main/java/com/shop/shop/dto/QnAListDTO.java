package com.shop.shop.dto;

import com.shop.shop.domain.list.QnAList;
import com.shop.shop.domain.list.QnAListStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnAListDTO {

    private Long qnaListId;
    private String title;
    private String writer;
    private LocalDateTime date;
//    private boolean answer;
    private String content;
    private Long parentId;
    private Long memberId;
    private Long itemId;
    private boolean delFlag;
    private QnAListStatus qnAListStatus;

    public QnAListDTO(QnAList qnAList) {
        this.qnaListId = qnAList.getId();
        this.title = qnAList.getTitle();
        this.writer = qnAList.getWriter();
        this.date = qnAList.getDate();
//        this.answer = qnAList.isAnswer();
        this.content = (qnAList.getContent() == null) ? null : qnAList.getContent();
        this.parentId = (qnAList.getParent() == null) ? null : qnAList.getParent().getId();
        this.memberId = (qnAList.getMember() == null) ? null : qnAList.getMember().getId();
        this.itemId = (qnAList.getItem() == null) ? null : qnAList.getItem().getId();
        this.delFlag = qnAList.isDelFlag();
        this.qnAListStatus = (qnAList.getQnAListStatus() == null) ? QnAListStatus.WAITING_ANSWER : qnAList.getQnAListStatus();
    }

}

package com.shop.shop.service;

import com.shop.shop.domain.item.Item;
import com.shop.shop.domain.list.QnAList;
import com.shop.shop.domain.list.QnAListStatus;
import com.shop.shop.domain.member.Member;
import com.shop.shop.domain.member.MemberRole;
import com.shop.shop.dto.OrderDTO;
import com.shop.shop.dto.QnAListDTO;
import com.shop.shop.repository.ItemRepository;
import com.shop.shop.repository.MemberRepository;
import com.shop.shop.repository.QnAListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class QnAListServiceImpl implements QnAListService {

    private final QnAListRepository qnAListRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 질의응답 리스트 등록
    @Override
    public QnAListDTO createQnAList(QnAListDTO qnAListDTO) {
        Member member = memberRepository.findById(qnAListDTO.getMemberId()).orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
        Item item = itemRepository.findById(qnAListDTO.getItemId()).orElseThrow(() -> new RuntimeException("해당 상품을 찾을 수 없습니다."));

        // 현재글의 기본 설정 세팅
        QnAList qnAList = QnAList.builder()
                .title(qnAListDTO.getTitle())
                .writer(qnAListDTO.getWriter())
                .date(LocalDateTime.now())
                .content(qnAListDTO.getContent())
                .item(item)
                .member(member)
                .qnAListStatus(checkMemberRole(member))
                .build();

        if (qnAListDTO.getParentId() != null) { // parentId가 존재한다면, 즉. 현재 작성글이 답변이라면
            QnAList parentQnAList = qnAListRepository.findById(qnAListDTO.getParentId()).orElseThrow(() -> new RuntimeException("해당 질의응답을 찾을 수 없습니다.")); // 목표한 질문글의 정보 가져오기
            qnAList.changeParent(parentQnAList); // 현재글을 목표한 글의 하위에 등록
            parentQnAList.changeQnAListStatus(QnAListStatus.ANSWER_COMPLETED); // 가져온 질문글의 상태를 '답변완료'로 변경
            qnAListRepository.save(parentQnAList); // 변경된 상태를 저장
        }

        // 최종 저장
        QnAList savedQnAList = qnAListRepository.save(qnAList);

        return new QnAListDTO(savedQnAList);
    }

    // 현재글을 작성하는 사용자의 역할에 따라 글 상태를 정하는 메서드
    private QnAListStatus checkMemberRole(Member member) {
        if (member.getMemberRoleList() == null || member.getMemberRoleList().isEmpty()) {
            throw new RuntimeException("작성자의 역할을 인식할 수 없습니다.");
        }
        if (member.getMemberRoleList().get(0) == MemberRole.USER) {
            return QnAListStatus.WAITING_ANSWER;
        } else {
            return QnAListStatus.ANSWER;
        }
    }

    // 질의응답 리스트 모두 조회(페이징) 삭제 포함
    @Override
    public Page<QnAListDTO> getAllQnAListPage(Pageable pageable) {
        Page<QnAList> qnAListDTOPage = qnAListRepository.findAllQnAListPage(pageable);
        if (qnAListDTOPage == null || qnAListDTOPage.isEmpty()) {
            throw new RuntimeException("조회된 질의응답 글이 없습니다.");
        }
        return qnAListDTOPage.map(QnAListDTO::new);
    }

    // 질의응답 리스트 모두 조회(페이징) 삭제 미포함
    @Override
    public Page<QnAListDTO> getAllQnAListPageWithDelFlag(Pageable pageable) {
        Page<QnAList> qnAListDTOPage = qnAListRepository.findAllQnAListPageWithDelFlag(pageable);
        if (qnAListDTOPage == null || qnAListDTOPage.isEmpty()) {
            throw new RuntimeException("조회된 질의응답 글이 없습니다.");
        }
        return qnAListDTOPage.map(QnAListDTO::new);
    }

    // 질의응답Id를 기준으로 조회 삭제 포함
    @Override
    public QnAListDTO getQnAListByQnAListId(Long qnaListId) {
        QnAList qnAList = qnAListRepository.findByQnAListId(qnaListId);
        if (qnAList == null) {
            throw new RuntimeException("조회된 질의응답 글이 없습니다.");
        }
        return new QnAListDTO(qnAList);
    }

    // 질의응답Id를 기준으로 조회 삭제 미포함
    @Override
    public QnAListDTO getQnAListByQnAListIdWithDelFlag(Long qnaListId) {
        QnAList qnAList = qnAListRepository.findByQnAListIdWithDelFlag(qnaListId);
        if (qnAList == null) {
            throw new RuntimeException("조회된 질의응답 글이 없습니다.");
        }
        return new QnAListDTO(qnAList);
    }

    // 질의응답 수정
    @Override
    public QnAListDTO editQnAList(QnAListDTO qnAListDTO) {
        Member member = memberRepository.findById(qnAListDTO.getMemberId()).orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
        boolean checkWriting = checkWritingStatus(qnAListDTO.getMemberId(), qnAListDTO.getQnaListId());
        if (!checkWriting && member.getMemberRoleList().get(0) == MemberRole.USER) {
            throw new RuntimeException("해당 글을 수정할 권한이 없습니다.");
        }
        // 수정하고자 하는 질의응답 글 조회
        QnAList qnAList = qnAListRepository.findById(qnAListDTO.getQnaListId()).orElseThrow(() -> new RuntimeException("해당 질의응답 리스트를 찾을 수 없습니다."));
        // 필드 수정
        qnAList.changeTitle(qnAListDTO.getTitle());
        qnAList.changeWriter(qnAListDTO.getWriter());
        qnAList.changeContent(qnAListDTO.getContent());
        if (qnAListDTO.getQnAListStatus() != null) { // 글 상태를 변경하고자 한다면, 즉.변경값이 있다면
            qnAList.changeQnAListStatus(qnAListDTO.getQnAListStatus());
        }
        if (qnAListDTO.getParentId() != null) { // 값이 존재할경우 목표글의 하위글 = 답변글
            // 목표하고자 하는 글의Id를 기준으로 해당 글 조회
            QnAList getQnAList = qnAListRepository.findById(qnAListDTO.getParentId()).orElseThrow(() -> new RuntimeException("해당 질의응답 글을 찾을 수 없습니다."));
            qnAList.changeParent(getQnAList); // 해당글을 현재글의 상위로 등록
        }
        qnAList.changeDelFlag(qnAListDTO.isDelFlag());
        QnAList savedQnAList = qnAListRepository.save(qnAList);

        return new QnAListDTO(savedQnAList);
    }

    // 특정 질의응답 리스트 삭제(논리적 삭제)
    @Override
    public void deleteQnAList(QnAListDTO qnAListDTO) {
        Member member = memberRepository.findById(qnAListDTO.getMemberId()).orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
        boolean checkWriting = checkWritingStatus(qnAListDTO.getMemberId(), qnAListDTO.getQnaListId());
        if (!checkWriting && member.getMemberRoleList().get(0) == MemberRole.USER) {
            throw new RuntimeException("해당 글을 삭제할 권한이 없습니다.");
        }
        QnAList qnAList = qnAListRepository.findById(qnAListDTO.getQnaListId()).orElseThrow(() -> new RuntimeException("해당 질의응답 리스트를 찾을 수 없습니다."));
        List<QnAList> subQnAList = qnAListRepository.findAllByParentId(qnAList.getId()); // 삭제하려는 글을 상위로 가지고있는 하위 글을 조회
        if (subQnAList != null && !subQnAList.isEmpty()) { // 하위글이 존재한다면
            for (QnAList deleteQnAList : subQnAList) {
                deleteQnAList.changeParent(null); // 상위글과 연관관계삭제
                deleteQnAList.changeDelFlag(true); // 같이 삭제처리
                qnAListRepository.save(deleteQnAList); // 변경사항 저장}
            }
        qnAList.changeDelFlag(true);
        qnAListRepository.save(qnAList);
        }
    }

    // 해당 질문글을 작성했는지 여부
    @Override
    public boolean checkWritingStatus(Long memberId, Long qnaListId) {
        List<QnAList> qnAList = qnAListRepository.findAllByMemberId(memberId);
        if (qnAList == null || qnAList.isEmpty()) {
            throw new RuntimeException("해당 회원의 질의응답 내역이 존재하지 않습니다.");
        }
        boolean checkStatus = false;
        for (QnAList targetQnAList : qnAList) {
            if (targetQnAList == null) {
                throw new RuntimeException("주문 상품을 조회할 수 없습니다.");
            } else {
                if (targetQnAList.getId() == qnaListId) {
                    checkStatus = true;
                }
            }
        }
        return checkStatus;
    }
}

package com.shop.shop.repository;

import com.shop.shop.domain.list.QnAList;
import com.shop.shop.dto.QnAListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnAListRepository extends JpaRepository<QnAList, Long> {

    // 삭제 처리여부와 상관없이 질의응답 리스트 모두 조회(페이징)
    @Query("SELECT ql FROM QnAList ql")
    Page<QnAList> findAllQnAListPage(Pageable pageable);

    // 삭제 처리를 제외한 질의응답 리스트 모두 조회(페이징)
    @Query("SELECT ql FROM QnAList ql WHERE ql.delFlag = false")
    Page<QnAList> findAllQnAListPageWithDelFlag(Pageable pageable);

    // 삭제 처리여부와 상관 없이 특정 질의응답Id를 기준으로 조회
    @Query("SELECT ql FROM QnAList ql WHERE ql.id = :qnaListId")
    QnAList findByQnAListId(@Param("qnaListId") Long qnaListId);

    // 삭제 처리를 제외한 특정 질의응답Id를 기준으로 조회
    @Query("SELECT ql FROM QnAList ql WHERE ql.id = :qnaListId AND ql.delFlag = false")
    QnAList findByQnAListIdWithDelFlag(@Param("qnaListId") Long qnaListId);
    
    // parentId를 기준으로 하위 질의응답 조회
    @Query("SELECT ql FROM QnAList ql WHERE ql.parent.id = :parentId")
    QnAList findByParentId(@Param("parentId") Long parentId);

    // parentId를 기준으로 하위 질의응답 모두 조회
    @Query("SELECT ql FROM QnAList ql WHERE ql.parent.id = :parentId")
    List<QnAList> findAllByParentId(@Param("parentId") Long parentId);

    // itemId를 기준으로 질의응답 모두 조회
    @Query("SELECT ql FROM QnAList ql WHERE ql.item.id = :itemId")
    List<QnAList> findAllByItemId(@Param("itemId") Long itemId);

    // memberId를 기준으로 질의응답 모두 조회
    @Query("SELECT ql FROM QnAList ql WHERE ql.member.id = :memberId")
    List<QnAList> findAllByMemberId(@Param("memberId") Long memberId);

}

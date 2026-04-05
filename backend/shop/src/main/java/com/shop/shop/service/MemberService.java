package com.shop.shop.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import com.shop.shop.domain.member.Member;
import com.shop.shop.dto.MemberDTO;
import com.shop.shop.dto.MemberModifyDTO;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
public interface MemberService {

    MemberDTO getKakaoMember(String accessToken);

    void modifyMember(MemberModifyDTO memberModifyDTO);

    void makeMember(MemberDTO memberDTO);

    // Member 엔티티를 MemberDTO 로 바꾸는 메서드
    default MemberDTO entityToDTO(Member member) {
        MemberDTO dto = new MemberDTO(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getMemberName(),
                member.getPhoneNumber(),
                member.getAddress(),
                member.getStockMileage(),
                member.getJoinDate(),
                member.isWtrSns(),
                member.isSocial(),
                member.isDelFlag(),
                member.getMemberShip(),
                member.getMemberRoleList().stream()
                        .map(memberRole -> memberRole.name())
                        .collect(Collectors.toList())
        );
        return dto;
    }

    Long saveMember(MemberDTO memberDTO); // 일반 회원가입

    List<MemberDTO> getAllMembers(); // 모든 회원 조회

    public Page<MemberDTO> getAllMembersPage(Pageable pageable); // 모든 회원 조회(페이징)

    MemberDTO getMemberById(Long id); // ID를 기준으로 특정 회원 조회

    MemberDTO getMemberByEmail(String email); // Email 을 기준으로 특정 회원 조회

    void deleteMember(Long id); // ID를 기준으로 회원 삭제 처리

    void updateMember(MemberDTO memberDTO); // 관리자가 회원 정보 수정

    List<MemberDTO> getMembersByName(String name); // 이름을 기준으로 모든 회원 조회

    boolean existsByEmail(String email); // Email 을 기준으로 회원 여부

    boolean existsById(Long id); // ID를 기준으로 회원 여부

}

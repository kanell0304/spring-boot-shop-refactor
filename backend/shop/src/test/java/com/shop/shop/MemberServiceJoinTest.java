package com.shop.shop;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import com.shop.shop.domain.member.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.shop.shop.domain.member.Address;
import com.shop.shop.domain.member.Member;
import com.shop.shop.domain.member.MemberShip;
import com.shop.shop.dto.MemberDTO;
import com.shop.shop.repository.MemberRepository;
import com.shop.shop.service.MemberService;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Transactional
@Log4j2
public class MemberServiceJoinTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanUp() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 - MemberService.makeMember 테스트")
    void testMakeMember() {

        Member member = Member.builder()
                .email("test@unit.com")
                .password(passwordEncoder.encode("1234"))
                .memberName("유닛테스트")
                .phoneNumber("010-1234-5678")
                .joinDate(LocalDateTime.now())
                .memberShip(MemberShip.BRONZE)
                .address(new Address("12345", "서울시 강남구", "테스트타워 1층"))
                .stockMileage(0)
                .wtrSns(false)
                .social(false)
                .delFlag(false)
                .build();
        member.addRole(MemberRole.USER);

        memberRepository.save(member);

        MemberDTO found = memberService.getMemberByEmail("test@unit.com");

        System.out.println("조회된 이메일: " + found.getEmail());
        System.out.println("조회된 이름: " + found.getMemberName());

        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("test@unit.com");
        assertThat(found.getMemberName()).isEqualTo("유닛테스트");
    }

    @Test
    @DisplayName("이메일 중복 검사 - 존재하면 true 반환")
    void testExistsByEmail() {
        // given
        MemberDTO memberDTO = new MemberDTO(
                null,
                "dup@unit.com",
                passwordEncoder.encode("1234"),
                "중복유저",
                "010-1111-2222",
                new Address("11111", "중복시", "중복빌딩"),
                0,
                LocalDateTime.now(),
                false,
                false,
                false,
                MemberShip.SILVER,
                Collections.singletonList("USER")
        );

        memberService.makeMember(memberDTO);

        // when
        boolean exists = memberService.existsByEmail("dup@unit.com");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("이름으로 회원 조회 테스트")
    void testGetMembersByName() {
        // 2명의 동일한 이름 유저 추가
        for (int i = 0; i < 2; i++) {
            MemberDTO memberDTO = new MemberDTO(
                    null,
                    "name" + i + "@unit.com",
                    passwordEncoder.encode("1234"),
                    "동명이인",
                    "010-7777-777" + i,
                    new Address("77777", "서울시", "테스트건물"),
                    0,
                    LocalDateTime.now(),
                    false,
                    false,
                    false,
                    MemberShip.BRONZE,
                    Collections.singletonList("USER")
            );
            memberService.makeMember(memberDTO);
        }

        // 조회
        List<MemberDTO> result = memberService.getMembersByName("동명이인");

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("회원 삭제 - delFlag가 true로 설정되는지 확인")
    void testDeleteMember() {
        // 회원 생성
        MemberDTO memberDTO = new MemberDTO(
                null,
                "delete@unit.com",
                passwordEncoder.encode("1234"),
                "삭제유저",
                "010-0000-0000",
                new Address("00000", "삭제시", "삭제건물"),
                0,
                LocalDateTime.now(),
                false,
                false,
                false,
                MemberShip.GOLD,
                Collections.singletonList("USER")
        );

        memberService.makeMember(memberDTO);
        Member member = memberRepository.findByEmail("delete@unit.com");
        Long memberId = member.getId();

        // 삭제 수행
        memberService.deleteMember(memberId);

        // 다시 조회
        Member deleted = memberRepository.findById(memberId).orElse(null);

        assertThat(deleted).isNotNull();
        assertThat(deleted.isDelFlag()).isTrue();
    }
}

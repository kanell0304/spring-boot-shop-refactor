package com.shop.shop.service;

import com.shop.shop.domain.member.Address;
import com.shop.shop.domain.member.Member;
import com.shop.shop.domain.member.MemberRole;
import com.shop.shop.domain.member.MemberShip;
import com.shop.shop.dto.MemberDTO;
import com.shop.shop.dto.MemberModifyDTO;
import com.shop.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    // 카카오 엑세스 토큰으로 회원 정보 가져오기
    @Override
    public MemberDTO getKakaoMember(String accessToken) {
        String email = getEmailFromKakaoAccessToken(accessToken); // accessToken 으로 이메일 추출
        Optional<Member> result = Optional.ofNullable(memberRepository.findByEmail(email)); // 이메일로 db에서 엔티티 조회

        if (result.isPresent()) { // 값이 있으면 -> 기존회원
            MemberDTO memberDTO = entityToDTO(result.get()); // dto 변환해서 반환
            return memberDTO;
        }

        Member socialMember = makeSocialMember(email); // 신규 회원 생성
        memberRepository.save(socialMember); // 엔티티 db에 저장

        MemberDTO memberDTO = entityToDTO(socialMember); // DTO 반환 후 반환
        return memberDTO;
    }

    // 카카오 엑세스 토큰으로 이메일 가져오기
    private String getEmailFromKakaoAccessToken(String accessToken) {
        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        if (accessToken == null) {
            throw new RuntimeException("Access Token is null");
        }

        RestTemplate restTemplate = new RestTemplate(); // http 요청을 보내기 위한 객체

        HttpHeaders headers = new HttpHeaders(); // 헤더 설정
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        // http 요청 or 응답을 나타내는 객체 생성 - header 만 설정하고 body 가 없는 상태
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // URL 을 동적으로 생성 - 이것을 기반으로 URI 객체 생성
        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

        // RestTemplate 으로 get 요청을 보냄
        // Json 응답을 -> LinkedHashMap 객체로 받음
        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(
                uriBuilder.toString(), // 요청 url
                HttpMethod.GET, // 요청 메서드
                entity, // 요청 헤더 정보
                LinkedHashMap.class // 응답(json)을 LinkedHashMap 타입으로 변환
        );

        // 작성x
        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        LinkedHashMap<String, String> kakaoAcount = bodyMap.get("kakao_account");

        return kakaoAcount.get("email");
    }

    // 임시 비밀번호 생성
    private String makeTempPassword() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            buffer.append((char) ((int) (Math.random() * 55) + 65));
            // 아스키 코드표 : 65~119 사이의 문자가 랜덤으로 10자리 생성 -> 임시비밀번호
        }
        return buffer.toString();
    }

    // 일반 회원가입
    @Override
    public void makeMember(MemberDTO memberDTO) {
        validateDuplicateMember(memberDTO); // 중복 회원 검증 로직 실행
        if (memberDTO.getAddress() == null) {
            memberDTO.setAddress(new Address("설정되지 않음", "설정되지 않음", "설정되지 않음"));
        }

        Member member = Member.builder()
                .email(memberDTO.getEmail())
                .password(passwordEncoder.encode(memberDTO.getPassword()))
                .memberName(memberDTO.getMemberName())
                .phoneNumber(memberDTO.getPhoneNumber())
                .joinDate(LocalDateTime.now())
                .memberShip(MemberShip.BRONZE)
                .address(new Address(memberDTO.getAddress().getZip_code(), memberDTO.getAddress().getDefault_address(), memberDTO.getAddress().getDetailed_address()))
                .wtrSns(memberDTO.isWtrSns())
                .social(false)
                .delFlag(false)
                .build();

        // 전달받은 회원역할에따라 일반회원인지 관리자인지 구별 후 생성
        if (MemberRole.valueOf(memberDTO.getRoleNames().get(0)) == MemberRole.ADMIN) {
            member.addRole(MemberRole.ADMIN);
        } else {
            member.addRole(MemberRole.USER);
        }

        memberRepository.save(member);
    }

    // 소셜 회원 생성
    private Member makeSocialMember(String email) {
        String tempPassword = makeTempPassword();
        log.info("tempPassword : " + tempPassword);
        int randomId = (int) (Math.random() * 1000 + 1);
        String memberName = "소셜회원#" + randomId;

        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(tempPassword))
                .memberName(memberName)
                .phoneNumber("설정되지 않음")
                .joinDate(LocalDateTime.now())
                .memberShip(MemberShip.BRONZE)
                .address(new Address("설정되지 않음", "설정되지 않음", "설정되지 않음"))
                .wtrSns(false)
                .social(true)
                .delFlag(false)
                .build();
        member.addRole(MemberRole.USER);

        return member;
    }

    // (회원페이지 or 소셜 회원가입 직후 수정 페이지) 회원 정보 수정
    @Override
    public void modifyMember(MemberModifyDTO memberModifyDTO) {
        Member result = memberRepository.findByEmail(memberModifyDTO.getEmail());
        log.info("SNS 동의 여부 " + memberModifyDTO.isWtrSns());

        result.changePassword(passwordEncoder.encode(memberModifyDTO.getPassword()));
        result.changePhoneNumber(memberModifyDTO.getPhoneNumber());
        result.changeWtrSns(memberModifyDTO.isWtrSns());
        result.getAddress().setZip_code(memberModifyDTO.getZip_code());
        result.getAddress().setDefault_address(memberModifyDTO.getDefault_address());
        result.getAddress().setDetailed_address(memberModifyDTO.getDetailed_address());
        result.changeStockMileage(memberModifyDTO.getStockMileage());

        if (memberModifyDTO.getMemberShip() != null) {
            result.changeMemberShip(memberModifyDTO.getMemberShip());
        }
        memberRepository.save(result);
    }

    // 회원 등록
    @Transactional
    @Override
    public Long saveMember(MemberDTO memberDTO) {
        validateDuplicateMember(memberDTO); // 중복 회원 검증 로직 실행
        Member member = Member.builder()
                .email(memberDTO.getEmail())
                .password(memberDTO.getPassword())
                .memberName(memberDTO.getMemberName())
                .phoneNumber(memberDTO.getPhoneNumber())
                .address(memberDTO.getAddress())
                .memberRoleList(memberDTO.getRoleNames().stream()
                        .map(MemberRole::valueOf) // String → Enum 변환
                        .collect(Collectors.toList()))
                .build();
        member.addRole(MemberRole.USER);
        memberRepository.save(member);
        return member.getId();
    }

    // 중복회원 여부 검사 메서드
    private void validateDuplicateMember(MemberDTO memberDTO) {
        boolean existsMember = memberRepository.existsByEmail(memberDTO.getEmail());
//        List<Member> foundMember = (List<Member>) memberRepository.findByEmail(memberDTO.getEmail());
        if (existsMember) {
            throw new IllegalArgumentException("이미 존재하는 회원");
        }
    }

    // 모든 회원 조회
    @Override
    public List<MemberDTO> getAllMembers() {
        List<Member> member = memberRepository.findAll();
        List<MemberDTO> memberDTO = new ArrayList<>();
        for (Member memberList : member) {
            memberDTO.add(entityToDTO(memberList));
        }
        return memberDTO;
    }

    // 모든 회원 조회(페이징)
    @Override
    public Page<MemberDTO> getAllMembersPage(Pageable pageable) {
        Page<Member> memberPage = memberRepository.findAllMemberPage(pageable);
        if (memberPage == null) {
            throw new RuntimeException("회원 조회를 실패하였습니다.");
        }
        Page<MemberDTO> memberDTO = memberPage.map(this::entityToDTO);
        return memberDTO;
    }

    // 특정 회원 조회
    @Override
    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        return entityToDTO(member);
    }

    // 이메일로 회원 조회
    @Override
    public MemberDTO getMemberByEmail(String email) {
            Member member = memberRepository.findByEmail(email);
            if (member == null) {
                throw new RuntimeException("회원을 찾을 수 없습니다.");
            }
            return entityToDTO(member);
    }

    // 회원 id 를 기준으로 회원 삭제(논리적 삭제)
    @Transactional
    @Override
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        member.changeDelFlag(true);
        memberRepository.save(member);
    }

    // (관리자 페이지 - 회원 번호 기준)회원 정보 수정
    @Transactional
    @Override
    public void updateMember(MemberDTO memberDTO) {
        // 변경하려는 회원이 존재하는 여부
        if (!existsByEmail(memberDTO.getEmail())) { // 회원이 존재 하지 않으면
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        Member searchMember = memberRepository.findByEmail(memberDTO.getEmail()); // 회원 번호를 기준으로 회원 조회

        searchMember.changePassword(memberDTO.getPassword());
        searchMember.changePhoneNumber(memberDTO.getPhoneNumber());
        searchMember.changeMemberName(memberDTO.getMemberName());
        searchMember.changeWtrSns(memberDTO.isWtrSns());
        searchMember.changeStockMileage(memberDTO.getStockMileage()); // 마일리지 값은 항상 존재
        // 수정 페이지에서 마일리지 잔고를 수정 가능하며 기본값은 현재 마일리지로 값이 대입되어 있어야함

        if (memberDTO.getAddress() != null) { // 넘어온 address 값이 있으면
            Address address = modelMapper.map(memberDTO.getAddress(), Address.class);

            searchMember.getAddress().setZip_code(address.getZip_code());
            searchMember.getAddress().setDefault_address(address.getDefault_address());
            searchMember.getAddress().setDetailed_address(address.getDetailed_address());
        }

        if (memberDTO.getMemberShip() != null) { // 넘어온 회원 등급 값이 있다면
            searchMember.changeMemberShip(memberDTO.getMemberShip());
        }

        memberRepository.save(searchMember);
    }

    // 회원을 이름으로 모두 조회
    @Override
    public List<MemberDTO> getMembersByName(String memberName) {
        List<Member> memberList = memberRepository.findAllByMemberName(memberName);
        List<MemberDTO> memberDTO = new ArrayList<>();
        for (Member searchMemberList : memberList) {
            memberDTO.add(entityToDTO(searchMemberList));
        }
        return memberDTO;
    }

    // 특정 회원 존재 여부(id)
    @Override
    public boolean existsById(Long id) {
        return memberRepository.existsById(id);
    }

    // 특정 회원 존재 여부(email)
    @Override
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

}

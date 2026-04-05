package com.shop.shop.security;

import com.shop.shop.domain.member.Member;
import com.shop.shop.dto.MemberDTO;
import com.shop.shop.dto.MemberDetails;
import com.shop.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * CustomUSerDetailsService
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        log.info("----------------loadUserByUsername-----------------------------");

        Member member = memberRepository.getWithRoles(email);

        log.info("회원정보를 찾았습니다.");

        MemberDTO memberDTO = new MemberDTO(
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

        log.info(memberDTO);

        return new MemberDetails(memberDTO);

    }

}

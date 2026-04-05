package com.shop.shop.config;

import com.shop.shop.domain.member.Address;
import com.shop.shop.domain.member.Member;
import com.shop.shop.domain.member.MemberRole;
import com.shop.shop.domain.member.MemberShip;
import com.shop.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Log4j2
public class AdminInitializer implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (!memberRepository.existsByEmail("admin")) {
            Member admin = Member.builder()
                    .email("admin")
                    .password(passwordEncoder.encode("admin"))
                    .memberName("관리자")
                    .phoneNumber("000-0000-0000")
                    .joinDate(LocalDateTime.now())
                    .memberShip(MemberShip.BRONZE)
                    .address(new Address("00000", "관리자 주소", "관리자 주소"))
                    .wtrSns(false)
                    .social(false)
                    .delFlag(false)
                    .build();
            admin.addRole(MemberRole.ADMIN);
            memberRepository.save(admin);
            log.info("=== Admin account created: email=admin / password=admin ===");
        } else {
            log.info("=== Admin account already exists, skipping initialization ===");
        }
    }
}

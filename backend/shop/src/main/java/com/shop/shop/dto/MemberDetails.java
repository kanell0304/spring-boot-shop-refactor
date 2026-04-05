package com.shop.shop.dto;

import com.shop.shop.domain.member.Address;
import com.shop.shop.domain.member.Member;
import com.shop.shop.domain.member.MemberShip;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MemberDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final String memberName;
    private final String phoneNumber;
    private final Address address;
    private final int stockMileage;
    private final LocalDateTime joinDate;
    private final boolean wtrSns;
    private final boolean social;
    private final boolean delFlag;
    private final MemberShip memberShip;
    private final List<GrantedAuthority> authorities;

    public MemberDetails(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.memberName = member.getMemberName();
        this.phoneNumber = member.getPhoneNumber();
        this.stockMileage = member.getStockMileage();
        this.joinDate = member.getJoinDate();
        this.wtrSns = member.isWtrSns();
        this.social = member.isSocial();
        this.delFlag = member.isDelFlag();
        this.memberShip = member.getMemberShip();
        this.address = member.getAddress() != null ? member.getAddress() : new Address();
        this.authorities = member.getMemberRoleList().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    public MemberDetails(MemberDTO memberDTO) {
        this.id = memberDTO.getId();
        this.email = memberDTO.getEmail();
        this.password = memberDTO.getPassword();
        this.memberName = memberDTO.getMemberName();
        this.phoneNumber = memberDTO.getPhoneNumber();
        this.stockMileage = memberDTO.getStockMileage();
        this.joinDate = memberDTO.getJoinDate();
        this.wtrSns = memberDTO.isWtrSns();
        this.social = memberDTO.isSocial();
        this.delFlag = memberDTO.isDelFlag();
        this.memberShip = memberDTO.getMemberShip();
        this.address = memberDTO.getAddress() != null ? memberDTO.getAddress() : new Address();
        this.authorities = memberDTO.getRoleNames().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    /// /    public MemberDetails(String email, String password, String memberName, String phoneNumber, Address address, int stockMileage, LocalDateTime joinDate, boolean wtrSns, boolean social, boolean delFlag, MemberShip memberShip, List<GrantedAuthority> authorities) {
//    public MemberDetails(Member member) {
//        this.email = member.getEmail();
//        this.password = password;
//        this.memberName = memberName;
//        this.phoneNumber = phoneNumber;
//        this.address = member.getAddress() != null ? member.getAddress() : new Address();
//        this.stockMileage = stockMileage;
//        this.joinDate = joinDate;
//        this.wtrSns = wtrSns;
//        this.social = social;
//        this.delFlag = delFlag;
//        this.memberShip = memberShip;
//        this.authorities = authorities;
//    }
    public MemberDTO toMemberDTO() {
        return new MemberDTO(
                this.id,
                this.email,
                this.password,
                this.memberName,
                this.phoneNumber,
                this.address != null ? this.address : new Address(),
                this.stockMileage,
                this.joinDate,
                this.wtrSns,
                this.social,
                this.delFlag,
                this.memberShip,
                this.authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(role -> role.replace("ROLE_", ""))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

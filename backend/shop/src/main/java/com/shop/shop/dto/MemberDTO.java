package com.shop.shop.dto;

import com.shop.shop.domain.member.Address;
import com.shop.shop.domain.member.Member;
import com.shop.shop.domain.member.MemberShip;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
//@NoArgsConstructor
//@AllArgsConstructor
public class MemberDTO extends User {
    private Long id;
    private String email;
    private String password;
    private String memberName;
    private String phoneNumber;
    private Address address;
    private int stockMileage;
    private LocalDateTime joinDate;
    private boolean wtrSns;
    private boolean social;
    private boolean delFlag;

    private MemberShip memberShip;

    private List<String> roleNames = new ArrayList<>();

    public MemberDTO(Long id,String email, String password, String memberName, String phoneNumber, Address address, int stockMileage, LocalDateTime joinDate, boolean wtrSns, boolean social, boolean delFlag, MemberShip memberShip, List<String> roleNames) {
        super(
                email,
                password,
                (roleNames == null ? new ArrayList<>() : roleNames).stream().map(str -> new SimpleGrantedAuthority("ROLE_" + str)).collect(Collectors.toList()));
        this.id = id;
        this.email = email;
        this.password = password;
        this.memberName = memberName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.stockMileage = stockMileage;
        this.joinDate = joinDate;
        this.wtrSns = wtrSns;
        this.delFlag = delFlag;
        this.social = social;
        this.memberShip = memberShip;
        this.roleNames = (roleNames != null) ? roleNames : new ArrayList<>();
    }

    public Map<String, Object> getClaims() {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("memberId", id);  // id → memberId 로 변경
        dataMap.put("email", email);
        dataMap.put("password", password);
        dataMap.put("memberName", memberName);
        dataMap.put("social", social);
        dataMap.put("roleNames", roleNames);

        return dataMap;
    }

}

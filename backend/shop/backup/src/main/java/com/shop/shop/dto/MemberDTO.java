package com.shop.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NonNull
@AllArgsConstructor
public class MemberDTO {

    private String email;
    private String password;
    private String memberName;
    private String phoneNumber;
    private int stockMileage;
    private LocalDateTime joinDate;
    private boolean wtrSns;
    private boolean delFlag;

    private List<String> memberShip = new ArrayList<>();

//    private MemberDTO (String email, String password, String ) {
//
//    }

    public Map<String, Object> getClaims() {

        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("email", email);
        dataMap.put("password", password);
        dataMap.put("memberName", memberName);
//        dataMap.put("social", social);
        dataMap.put("memberShip", memberShip);

        return dataMap;
    }

}

package com.shop.shop.dto;

import com.shop.shop.domain.member.MemberShip;
import lombok.Data;

@Data
public class MemberModifyDTO {

    private String email;
    private String password;
    private String memberName;
    private String phoneNumber;
    private boolean wtrSns;
    private String zip_code;
    private String default_address;
    private String detailed_address;
    private boolean delFlag;
    private MemberShip memberShip;
    private int stockMileage;

}
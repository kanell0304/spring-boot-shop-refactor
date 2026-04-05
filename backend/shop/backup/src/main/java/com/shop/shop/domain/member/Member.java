package com.shop.shop.domain.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String password;
    private String memberName;
    private String phoneNumber;

    @Embedded
    private Address address;

    private int stockMileage;
    private LocalDateTime joinDate;
    private boolean wtrSns;
    private boolean delFlag;
    private boolean social;


    @Enumerated(EnumType.STRING)
    private MemberShip memberShip;

//    @ElementCollection(fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<MemberShip> memberShipList = new ArrayList<>();
//
//    public void addRole(MemberShip memberShip){
//
//        memberShipList.add(memberShip);
//    }

//    public void clearRole(){
//        memberShipList.clear();
//    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changeStockMileage(int stockMileage) {
        this.stockMileage = stockMileage;
    }

    public void changeWtrSns(boolean wtrSns) {
        this.wtrSns = wtrSns;
    }

    public void changeDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }

    public void changeSocial(boolean social) {
        this.social = social;
    }

}

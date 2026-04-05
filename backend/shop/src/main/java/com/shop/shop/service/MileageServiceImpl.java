package com.shop.shop.service;

import com.shop.shop.domain.member.Member;
import com.shop.shop.domain.member.Mileage;
import com.shop.shop.domain.member.MileageStatus;
import com.shop.shop.domain.order.Order;
import com.shop.shop.dto.MileageDTO;
import com.shop.shop.repository.MemberRepository;
import com.shop.shop.repository.MileageRepository;
import com.shop.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MileageServiceImpl implements MileageService {

    private final MileageRepository mileageRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    // 마일리지 내역 모두 조회
    @Override
    public Page<MileageDTO> findAllMileagePage(Pageable pageable) {
        Page<Mileage> mileagePage = mileageRepository.findAllMileagePage(pageable);
        Page<MileageDTO> mileageDTOPage = mileagePage.map(MileageDTO::new);
        return mileageDTOPage;
    }

    // 회원Id를 기준으로 마일리지 내역 전부 조회
    @Override
    public List<MileageDTO> findAllByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
        List<Mileage> mileageList = mileageRepository.findAllByMemberId(member.getId());
        if (mileageList == null || mileageList.isEmpty()) {
            throw new RuntimeException("해당 회원의 마일리지 내역이 존재하지 않습니다.");
        }
        return mileageList.stream().map(MileageDTO::new).collect(Collectors.toList());
    }

    // 회원Id를 기준으로 마일리지 내역 전부 조회(페이징)
    @Override
    public Page<MileageDTO> findAllByMemberId(Pageable pageable, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
        Page<Mileage> mileageList = mileageRepository.findAllByMemberId(pageable, member.getId());
        if (mileageList == null || mileageList.isEmpty()) {
            throw new RuntimeException("해당 회원의 마일리지 내역이 존재하지 않습니다.");
        }
        return mileageList.map(MileageDTO::new);
    }

    // 회원Email 을 기준으로 마일리지 내역 전부 조회
    @Override
    public List<MileageDTO> findAllByMemberEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new RuntimeException("해당 회원을 찾을 수 없습니다.");
        }
        List<Mileage> mileageList = mileageRepository.findAllByMemberId(member.getId());
        if (mileageList == null || mileageList.isEmpty()) {
            throw new RuntimeException("해당 회원의 마일리지 내역이 존재하지 않습니다.");
        }
        return mileageList.stream().map(MileageDTO::new).collect(Collectors.toList());
    }

    // 회원Email 을 기준으로 마일리지 내역 전부 조회(페이징)
    @Override
    public Page<MileageDTO> findAllByMemberEmail(Pageable pageable, String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new RuntimeException("해당 회원을 찾을 수 없습니다.");
        }
        Page<Mileage> mileageList = mileageRepository.findAllByMemberId(pageable, member.getId());
        if (mileageList == null || mileageList.isEmpty()) {
            throw new RuntimeException("해당 회원의 마일리지 내역이 존재하지 않습니다.");
        }
        return mileageList.map(MileageDTO::new);
    }

    // 주문Id를 기준으로 마일리지 내역 전부 조회
    @Override
    public List<MileageDTO> findAllByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("해당 주문을 찾을 수 없습니다."));
        List<Mileage> mileageList = mileageRepository.findAllByOrderId(order.getId());
        if (mileageList == null || mileageList.isEmpty()) {
            throw new RuntimeException("해당 주문의 마일리지 내역이 존재하지 않습니다.");
        }
        return mileageList.stream().map(MileageDTO::new).collect(Collectors.toList());
    }

    // 주문Id를 기준으로 마일리지 내역 전부 조회(페이징)
    @Override
    public Page<MileageDTO> findAllByOrderId(Pageable pageable, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("해당 주문을 찾을 수 없습니다."));
        Page<Mileage> mileageList = mileageRepository.findAllByOrderId(pageable, order.getId());
        if (mileageList == null || mileageList.isEmpty()) {
            throw new RuntimeException("해당 주문의 마일리지 내역이 존재하지 않습니다.");
        }
        return mileageList.map(MileageDTO::new);
    }

    // 마일리지 내역 생성
    @Override
    public MileageDTO createMileage(MileageDTO mileageDTO) {
        Member member = memberRepository.findById(mileageDTO.getMemberId()).orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
        Order order = orderRepository.findById(mileageDTO.getOrderId()).orElseThrow(() -> new RuntimeException("해당 주문을 찾을 수 없습니다."));
        Mileage mileage = Mileage.builder()
                .amount(mileageDTO.getAmount())
                .mileageDate(LocalDateTime.now())
                .mileageStatus(mileageDTO.getMileageStatus())
                .member(member)
                .order(order)
                .build();
        Mileage savedMileage = mileageRepository.save(mileage);
        return new MileageDTO(savedMileage);
    }

    // 마일리지 삭제
    @Override
    public void deleteMileageById(Long mileageId) {
        boolean existsMileage = mileageRepository.existsById(mileageId);
        if (!existsMileage) {
            throw new RuntimeException("해당 마일리지 내역을 찾을 수 없습니다.");
        }
        mileageRepository.deleteById(mileageId);
    }

    // 특정 기간 동안의 마일리지 내역 전부 조회
    @Override
    public List<MileageDTO> findByDuringPeriod(LocalDateTime mileageDate1, LocalDateTime mileageDate2) {
        List<Mileage> mileageList = mileageRepository.findByDuringPeriod(mileageDate1, mileageDate2);
        if (mileageList == null || mileageList.isEmpty()) {
            throw new RuntimeException("해당 기간의 마일리지 내역이 존재하지 않습니다.");
        }
        return mileageList.stream().map(MileageDTO::new).collect(Collectors.toList());
    }

    // 특정 기간 동안의 마일리지 내역 전부 조회(페이징)
    @Override
    public Page<MileageDTO> findByDuringPeriod(Pageable pageable, LocalDateTime mileageDate1, LocalDateTime mileageDate2) {
        Page<Mileage> mileageList = mileageRepository.findByDuringPeriod(pageable, mileageDate1, mileageDate2);
        if (mileageList == null || mileageList.isEmpty()) {
            throw new RuntimeException("해당 기간의 마일리지 내역이 존재하지 않습니다.");
        }
        return mileageList.map(MileageDTO::new);
    }

    // 특정 회원의 특정 기간 동안의 마일리지 내역 전부 조회
    @Override
    public List<MileageDTO> findByDuringPeriodFromMemberId(Long memberId, LocalDateTime mileageDate1, LocalDateTime mileageDate2) {
        List<Mileage> mileageList = mileageRepository.findByDuringPeriodFromMemberId(memberId, mileageDate1, mileageDate2);
        if (mileageList == null || mileageList.isEmpty()) {
            throw new RuntimeException("해당 회원 또는 해당 기간의 마일리지 내역이 존재하지 않습니다.");
        }
        return mileageList.stream().map(MileageDTO::new).collect(Collectors.toList());
    }

    // 특정 회원의 특정 기간 동안의 마일리지 내역 전부 조회(페이징)
    @Override
    public Page<MileageDTO> findByDuringPeriodFromMemberId(Pageable pageable, Long memberId, LocalDateTime mileageDate1, LocalDateTime mileageDate2) {
        Page<Mileage> mileageList = mileageRepository.findByDuringPeriodFromMemberId(pageable, memberId, mileageDate1, mileageDate2);
        if (mileageList == null || mileageList.isEmpty()) {
            throw new RuntimeException("해당 회원 또는 해당 기간의 마일리지 내역이 존재하지 않습니다.");
        }
        return mileageList.map(MileageDTO::new);
    }
}

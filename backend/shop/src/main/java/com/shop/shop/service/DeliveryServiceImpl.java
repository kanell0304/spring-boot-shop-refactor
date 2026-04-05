package com.shop.shop.service;

import com.shop.shop.domain.delivery.Delivery;
import com.shop.shop.dto.DeliveryDTO;
import com.shop.shop.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    // 배송상태 변경
    @Override
    public DeliveryDTO editStatus(DeliveryDTO deliveryDTO) {
        Delivery delivery = deliveryRepository.findById(deliveryDTO.getId()).orElseThrow(() -> new RuntimeException("해당 배송정보를 찾을 수 없습니다."));
        delivery.changeStatus(deliveryDTO.getDeliveryStatus());
        Delivery changedDelivery = deliveryRepository.save(delivery);
        return new DeliveryDTO(changedDelivery);
    }
}

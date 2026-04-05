package com.shop.shop.service;

import com.shop.shop.dto.DeliveryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeliveryService {

    public DeliveryDTO editStatus(DeliveryDTO deliveryDTO);

}

package com.shop.shop.domain.order;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipientAddress {

    private String recipient_zip_code;

    private String recipient_default_address;

    private String recipient_detailed_address;

}

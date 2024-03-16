package com.demo.carsharing.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PaymentRequestDto {

    private Long amount;
    private Long quantity;
    private String currency;
    private String name;
    private String successUrl;
    private String cancelUrl;
}

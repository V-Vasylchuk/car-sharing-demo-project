package com.demo.carsharing.dto.mapper;

import com.demo.carsharing.dto.request.PaymentRequestDto;
import com.demo.carsharing.dto.response.PaymentResponseDto;
import com.demo.carsharing.model.Payment;

public class PaymentMapper implements DtoMapper<Payment, PaymentRequestDto, PaymentResponseDto> {
    @Override
    public Payment toModel(PaymentRequestDto requestDto) {
        return new Payment()
                .setSessionUrl(requestDto.getSuccessUrl())
                .setAmount(requestDto.getAmount());
    }

    @Override
    public PaymentResponseDto toDto(Payment model) {
        return new PaymentResponseDto()
                .setSessionUrl(model.getSessionUrl())
                .setSessionId(model.getSessionId());
    }
}

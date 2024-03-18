package com.demo.carsharing.dto.mapper;

import com.demo.carsharing.dto.request.PaymentRequestDto;
import com.demo.carsharing.dto.response.PaymentResponseDto;
import com.demo.carsharing.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper implements DtoMapper<Payment, PaymentRequestDto, PaymentResponseDto> {
    @Override
    public Payment toModel(PaymentRequestDto requestDto) {
        return toModel(requestDto, new Payment());
    }

    @Override
    public Payment toModel(PaymentRequestDto requestDto, Payment model) {
        return model
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

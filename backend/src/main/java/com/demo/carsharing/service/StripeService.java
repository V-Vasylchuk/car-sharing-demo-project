package com.demo.carsharing.service;

import com.demo.carsharing.dto.request.PaymentRequestDto;
import com.demo.carsharing.dto.response.CapturePaymentResponseDto;
import com.demo.carsharing.dto.response.PaymentResponseDto;
import com.demo.carsharing.dto.response.StripeResponseDto;
import java.util.List;

public interface StripeService {

    StripeResponseDto<PaymentResponseDto> createPayment(
            PaymentRequestDto paymentRequest);

    StripeResponseDto<CapturePaymentResponseDto> capturePayment(String sessionId);

    PaymentResponseDto getById(long id);

    List<PaymentResponseDto> getAll();
}

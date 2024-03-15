package com.demo.carsharing.service;

import com.demo.carsharing.dto.request.PaymentRequestDto;
import com.demo.carsharing.dto.response.CapturePaymentResponseDto;
import com.demo.carsharing.dto.response.PaymentResponseDto;
import com.demo.carsharing.dto.response.StripeResponseDto;

public interface StripeService {

    StripeResponseDto<PaymentResponseDto> createPayment(
            PaymentRequestDto paymentRequest);

    StripeResponseDto<CapturePaymentResponseDto> capturePayment(String sessionId);
}

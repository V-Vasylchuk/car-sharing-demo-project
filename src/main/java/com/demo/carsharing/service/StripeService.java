package com.demo.carsharing.service;

import com.demo.carsharing.dto.stripe.CapturePaymentResponseDto;
import com.demo.carsharing.dto.stripe.CreatePaymentRequestDto;
import com.demo.carsharing.dto.stripe.CreatePaymentResponseDto;
import com.demo.carsharing.dto.stripe.StripeResponseDto;

public interface StripeService {

    StripeResponseDto<CreatePaymentResponseDto> createPayment(
            CreatePaymentRequestDto paymentRequest);

    StripeResponseDto<CapturePaymentResponseDto> capturePayment(String sessionId);
}

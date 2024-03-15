package com.demo.carsharing.controller;

import com.demo.carsharing.dto.request.PaymentRequestDto;
import com.demo.carsharing.dto.response.CapturePaymentResponseDto;
import com.demo.carsharing.dto.response.PaymentResponseDto;
import com.demo.carsharing.dto.response.StripeResponseDto;
import com.demo.carsharing.service.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stripe")
@RequiredArgsConstructor
public class StripeController {

    private final StripeService stripeService;

    @PostMapping("/create-payment")
    public ResponseEntity<StripeResponseDto<PaymentResponseDto>> createPayment(
            @RequestBody PaymentRequestDto createPaymentRequest) {
        StripeResponseDto<PaymentResponseDto> stripeResponse =
                stripeService.createPayment(createPaymentRequest);
        return ResponseEntity
                .status(stripeResponse.getHttpStatus())
                .body(stripeResponse);
    }

    @GetMapping("/capture-payment")
    public ResponseEntity<StripeResponseDto<CapturePaymentResponseDto>> capturePayment(
            @RequestParam String sessionId) {
        StripeResponseDto<CapturePaymentResponseDto> stripeResponse =
                stripeService.capturePayment(sessionId);
        return ResponseEntity
                .status(stripeResponse.getHttpStatus())
                .body(stripeResponse);
    }
}

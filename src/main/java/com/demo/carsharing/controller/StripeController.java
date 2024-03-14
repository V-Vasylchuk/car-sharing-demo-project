package com.demo.carsharing.controller;

import com.demo.carsharing.dto.stripe.CapturePaymentResponseDto;
import com.demo.carsharing.dto.stripe.CreatePaymentRequestDto;
import com.demo.carsharing.dto.stripe.CreatePaymentResponseDto;
import com.demo.carsharing.dto.stripe.StripeResponseDto;
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
    public ResponseEntity<StripeResponseDto<CreatePaymentResponseDto>> createPayment(
            @RequestBody CreatePaymentRequestDto createPaymentRequest) {
        StripeResponseDto<CreatePaymentResponseDto> stripeResponse =
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

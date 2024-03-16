package com.demo.carsharing.controller;

import com.demo.carsharing.dto.request.PaymentRequestDto;
import com.demo.carsharing.dto.response.CapturePaymentResponseDto;
import com.demo.carsharing.dto.response.PaymentResponseDto;
import com.demo.carsharing.dto.response.StripeResponseDto;
import com.demo.carsharing.service.StripeService;
import com.demo.carsharing.util.Constants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class StripeController {

    private final StripeService stripeService;

    @PostMapping("/create")
    public ResponseEntity<StripeResponseDto<PaymentResponseDto>> createPayment(
            @RequestBody PaymentRequestDto createPaymentRequest) {
        log.debug("Try create Payment");
        StripeResponseDto<PaymentResponseDto> stripeResponse =
                stripeService.createPayment(createPaymentRequest);
        log.debug("Payment was successfully created");
        return ResponseEntity
                .status(stripeResponse.getHttpStatus())
                .body(stripeResponse);
    }

    @GetMapping("/capture")
    public ResponseEntity<StripeResponseDto<CapturePaymentResponseDto>> capturePayment(
            @RequestParam String sessionId) {
        log.debug("Try capture Payment by sessionId {}", sessionId);
        StripeResponseDto<CapturePaymentResponseDto> stripeResponse =
                stripeService.capturePayment(sessionId);
        log.debug("Payment by sessionId {} was successfully captured", sessionId);
        return ResponseEntity
                .status(stripeResponse.getHttpStatus())
                .body(stripeResponse);
    }

    @GetMapping("/{id}")
    public PaymentResponseDto getById(@PathVariable(Constants.ENTITY_ID) int id) {
        log.debug("Try get Payment id {}", id);
        PaymentResponseDto paymentResponseDto = stripeService.getById(id);
        log.debug("Payment by id {} was successfully got", id);
        return paymentResponseDto;
    }

    @GetMapping
    public List<PaymentResponseDto> getAll() {
        log.debug("Try get all Payment");
        List<PaymentResponseDto> paymentResponseDtoList = stripeService.getAll();
        log.debug("All Payment was successfully got");
        return paymentResponseDtoList;
    }
}

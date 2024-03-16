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
        StripeResponseDto<PaymentResponseDto> stripeResponse =
                stripeService.createPayment(createPaymentRequest);
        return ResponseEntity
                .status(stripeResponse.getHttpStatus())
                .body(stripeResponse);
    }

    @GetMapping("/capture")
    public ResponseEntity<StripeResponseDto<CapturePaymentResponseDto>> capturePayment(
            @RequestParam String sessionId) {
        StripeResponseDto<CapturePaymentResponseDto> stripeResponse =
                stripeService.capturePayment(sessionId);
        return ResponseEntity
                .status(stripeResponse.getHttpStatus())
                .body(stripeResponse);
    }

    @GetMapping("/{id}")
    public PaymentResponseDto getById(@PathVariable(Constants.ENTITY_ID) int id) {
        log.debug("Try get PaymentResponseDto by id {}", id);
        PaymentResponseDto paymentResponseDto = stripeService.getById(id);
        log.debug("PaymentResponseDto by id {} was successfully got", id);
        return paymentResponseDto;
    }

    @GetMapping
    public List<PaymentResponseDto> getAll() {
        log.debug("Try get all PaymentResponseDto");
        List<PaymentResponseDto> paymentResponseDtoList = stripeService.getAll();
        log.debug("All PaymentResponseDto was successfully got");
        return paymentResponseDtoList;
    }
}

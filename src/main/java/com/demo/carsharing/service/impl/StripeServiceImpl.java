package com.demo.carsharing.service.impl;

import com.demo.carsharing.dto.mapper.PaymentMapper;
import com.demo.carsharing.dto.request.PaymentRequestDto;
import com.demo.carsharing.dto.response.CapturePaymentResponseDto;
import com.demo.carsharing.dto.response.PaymentResponseDto;
import com.demo.carsharing.dto.response.StripeResponseDto;
import com.demo.carsharing.repository.PaymentRepository;
import com.demo.carsharing.service.StripeService;
import com.demo.carsharing.util.Constants;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Value("${stripe.secretKey}")
    private String secretKey;

    @Override
    public StripeResponseDto<PaymentResponseDto> createPayment(
            PaymentRequestDto paymentRequestDto) {
        Stripe.apiKey = secretKey;
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                createProductData(paymentRequestDto);
        SessionCreateParams.LineItem.PriceData priceData =
                createPriceData(paymentRequestDto, productData);
        SessionCreateParams.LineItem lineItem = createLineItem(paymentRequestDto, priceData);
        SessionCreateParams params = createParams(paymentRequestDto, lineItem);
        Session session;
        try {
            session = Session.create(params);
        } catch (StripeException exception) {
            exception.printStackTrace();
            return createFailedPaymentResponse();
        }
        PaymentResponseDto responseDataDto = createPaymentResponseData(session);
        paymentRepository.save(paymentMapper.toModel(paymentRequestDto));
        return createSuccessPaymentResponse(responseDataDto);
    }

    @Override
    public StripeResponseDto<CapturePaymentResponseDto> capturePayment(String sessionId) {
        Stripe.apiKey = secretKey;
        try {
            Session session = Session.retrieve(sessionId);
            String status = session.getStatus();
            if (status.equalsIgnoreCase(Constants.STRIPE_SESSION_STATUS_SUCCESS)) {
                log.info(Constants.SUCCESSFULLY_CAPTURING_MESSAGE);
            }
            CapturePaymentResponseDto responseData = createCaptureResponseData(session,
                    sessionId, status);
            return createSuccessCapturePaymentResponse(responseData);
        } catch (StripeException exception) {
            exception.printStackTrace();
            return createFailedCapturePaymentResponse();
        }
    }

    private PaymentResponseDto createPaymentResponseData(Session session) {
        return new PaymentResponseDto()
                .setSessionId(session.getId())
                .setSessionUrl(session.getUrl());
    }

    private CapturePaymentResponseDto createCaptureResponseData(
            Session session, String sessionId, String status) {
        return new CapturePaymentResponseDto()
                .setSessionId(sessionId)
                .setSessionStatus(status)
                .setPaymentStatus(session.getPaymentStatus());
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(
            PaymentRequestDto paymentRequestDto) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(paymentRequestDto.getName())
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(
            PaymentRequestDto paymentRequestDto,
            SessionCreateParams.LineItem.PriceData.ProductData productData) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(paymentRequestDto.getCurrency())
                .setUnitAmount(paymentRequestDto.getAmount())
                .setProductData(productData)
                .build();
    }

    private SessionCreateParams.LineItem createLineItem(
            PaymentRequestDto paymentRequestDto,
            SessionCreateParams.LineItem.PriceData priceData) {
        return SessionCreateParams.LineItem
                .builder()
                .setQuantity(paymentRequestDto.getQuantity())
                .setPriceData(priceData)
                .build();
    }

    private SessionCreateParams createParams(PaymentRequestDto paymentRequestDto,
                                             SessionCreateParams.LineItem lineItem) {
        return SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(paymentRequestDto.getSuccessUrl())
                .setCancelUrl(paymentRequestDto.getCancelUrl())
                .addLineItem(lineItem)
                .build();
    }

    private StripeResponseDto<PaymentResponseDto> createSuccessPaymentResponse(
            PaymentResponseDto responseData) {
        return new StripeResponseDto<PaymentResponseDto>()
                .setStatus(Constants.SUCCESS)
                .setMessage(Constants.SUCCESSFULLY_CREATION_MESSAGE)
                .setHttpStatus(Constants.HTTP_STATUS_OK)
                .setData(responseData);
    }

    private StripeResponseDto<PaymentResponseDto> createFailedPaymentResponse() {
        return new StripeResponseDto<PaymentResponseDto>()
                .setStatus(Constants.FAILURE)
                .setMessage(Constants.FAILED_CREATION_MESSAGE)
                .setHttpStatus(Constants.HTTP_STATUS_BAD_REQUEST)
                .setData(null);
    }

    private StripeResponseDto<CapturePaymentResponseDto> createSuccessCapturePaymentResponse(
            CapturePaymentResponseDto responseData) {
        return new StripeResponseDto<CapturePaymentResponseDto>()
                .setStatus(Constants.SUCCESS)
                .setMessage(Constants.SUCCESSFULLY_CAPTURING_MESSAGE)
                .setHttpStatus(Constants.HTTP_STATUS_OK)
                .setData(responseData);
    }

    private StripeResponseDto<CapturePaymentResponseDto> createFailedCapturePaymentResponse() {
        return new StripeResponseDto<CapturePaymentResponseDto>()
                .setStatus(Constants.FAILURE)
                .setMessage(Constants.FAILED_CAPTURING_MESSAGE)
                .setHttpStatus(Constants.HTTP_STATUS_INTERNAL_SERVER_ERROR)
                .setData(null);
    }
}

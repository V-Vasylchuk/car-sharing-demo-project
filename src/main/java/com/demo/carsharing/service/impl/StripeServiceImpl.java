package com.demo.carsharing.service.impl;

import com.demo.carsharing.dto.stripe.CapturePaymentResponseDto;
import com.demo.carsharing.dto.stripe.CreatePaymentRequestDto;
import com.demo.carsharing.dto.stripe.CreatePaymentResponseDto;
import com.demo.carsharing.dto.stripe.StripeResponseDto;
import com.demo.carsharing.service.StripeService;
import com.demo.carsharing.util.Constants;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service

public class StripeServiceImpl implements StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    @Override
    public StripeResponseDto<CreatePaymentResponseDto> createPayment(
            CreatePaymentRequestDto paymentRequest) {
        Stripe.apiKey = secretKey;
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                createProductData(paymentRequest);
        SessionCreateParams.LineItem.PriceData priceData =
                createPriceData(paymentRequest, productData);
        SessionCreateParams.LineItem lineItem = createLineItem(paymentRequest, priceData);
        SessionCreateParams params = createParams(paymentRequest, lineItem);
        Session session;
        try {
            session = Session.create(params);
        } catch (StripeException exception) {
            exception.printStackTrace();
            return createFailedPaymentResponse();
        }
        CreatePaymentResponseDto responseData = createPaymentResponseData(session);
        return createSuccessPaymentResponse(responseData);
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

    private CreatePaymentResponseDto createPaymentResponseData(Session session) {
        return new CreatePaymentResponseDto()
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
            CreatePaymentRequestDto paymentRequest) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(paymentRequest.getName())
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(
            CreatePaymentRequestDto createPaymentRequest,
            SessionCreateParams.LineItem.PriceData.ProductData productData) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(createPaymentRequest.getCurrency())
                .setUnitAmount(createPaymentRequest.getAmount())
                .setProductData(productData)
                .build();
    }

    private SessionCreateParams.LineItem createLineItem(
            CreatePaymentRequestDto paymentRequest,
            SessionCreateParams.LineItem.PriceData priceData) {
        return SessionCreateParams.LineItem
                .builder()
                .setQuantity(paymentRequest.getQuantity())
                .setPriceData(priceData)
                .build();
    }

    private SessionCreateParams createParams(CreatePaymentRequestDto createPaymentRequest,
                                             SessionCreateParams.LineItem lineItem) {
        return SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(createPaymentRequest.getSuccessUrl())
                .setCancelUrl(createPaymentRequest.getCancelUrl())
                .addLineItem(lineItem)
                .build();
    }

    private StripeResponseDto<CreatePaymentResponseDto> createSuccessPaymentResponse(
            CreatePaymentResponseDto responseData) {
        return new StripeResponseDto<CreatePaymentResponseDto>()
                .setStatus(Constants.SUCCESS)
                .setMessage(Constants.SUCCESSFULLY_CREATION_MESSAGE)
                .setHttpStatus(Constants.HTTP_STATUS_OK)
                .setData(responseData);
    }

    private StripeResponseDto<CreatePaymentResponseDto> createFailedPaymentResponse() {
        return new StripeResponseDto<CreatePaymentResponseDto>()
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

package com.shipogle.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipogle.app.exception.PaymentGatewayException;
import com.shipogle.app.model.PaymentGatewayRequest;
import com.shipogle.app.model.PaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentGatewayClient {
    @Value("${payment.gateway.url}")
    private String paymentGatewayUrl;
    private ResponseEntity<String> responseEntity;
    private RestTemplate restTemplate;

    /**
     * setRestTemplate method is used to set the restTemplate
     *
     * @author Shivam Lakhanpal
     * @param restTemplate RestTemplate
     */
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * setPaymentGatewayUrl method is used to set the payment gateway url
     *
     * @author Shivam Lakhanpal
     * @param paymentGatewayUrl payment gateway url
     */
    public void setPaymentGatewayUrl(String paymentGatewayUrl) {
        this.paymentGatewayUrl = paymentGatewayUrl;
    }

    /**
     * chargeCreditCard method is used to charge the credit card
     *
     * @author Shivam Lakhanpal
     * @param paymentRequest payment request
     * @return PaymentResponse
     * @throws PaymentGatewayException payment gateway exception
     */
    public PaymentResponse chargeCreditCard(PaymentGatewayRequest paymentRequest) throws PaymentGatewayException {
        PaymentGatewayRequest paymentGatewayRequest = createPaymentGatewayRequest(paymentRequest);
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentType(MediaType.APPLICATION_JSON);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(paymentGatewayRequest);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);
            HttpMethod httpMethod = HttpMethod.POST;
            responseEntity = restTemplate.exchange(paymentGatewayUrl, httpMethod, requestEntity, String.class);
            PaymentResponse paymentResponse = objectMapper.readValue(responseEntity.getBody(), PaymentResponse.class);
            return paymentResponse;
        } catch (Exception e) {
            throw new PaymentGatewayException("Error processing payment: " + e.getMessage());
        }
    }

    /**
     * createPaymentGatewayRequest method is used to create the payment gateway request
     *
     * @author Shivam Lakhanpal
     * @param paymentRequest payment request
     * @return PaymentGatewayRequest
     */
    private PaymentGatewayRequest createPaymentGatewayRequest(PaymentGatewayRequest paymentRequest) {
        PaymentGatewayRequest paymentGatewayRequest = new PaymentGatewayRequest();
        paymentGatewayRequest.setAmount(paymentRequest.getAmount());
        paymentGatewayRequest.setCurrency(paymentRequest.getCurrency());
        paymentGatewayRequest.setCardNumber(paymentRequest.getCardNumber());
        paymentGatewayRequest.setCardExpiryMonth(paymentRequest.getCardExpiryMonth());
        paymentGatewayRequest.setCardExpiryYear(paymentRequest.getCardExpiryYear());
        paymentGatewayRequest.setCardCvv(paymentRequest.getCardCvv());
        paymentGatewayRequest.setCardHolderName(paymentRequest.getCardHolderName());
        return paymentGatewayRequest;
    }
}

package com.shipogle.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipogle.app.exception.PaymentGatewayException;
import com.shipogle.app.model.PaymentGatewayRequest;
import com.shipogle.app.model.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class PaymentGatewayClientTest {

    private PaymentGatewayClient paymentGatewayClient;
    @MockBean
    private RestTemplate restTemplate;
    private String paymentGatewayUrl = "http://mockUrl";

    private HttpEntity<String> requestEntity;
    private PaymentGatewayRequest paymentRequest;

    final private int TEST_CARD_EXPIRY_MONTH=12;
    final private int TEST_CARD_EXPIRY_YEAR=2022;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        MockitoAnnotations.openMocks(this);
        paymentGatewayClient = new PaymentGatewayClient();
        paymentGatewayClient.setRestTemplate(restTemplate);
        paymentGatewayClient.setPaymentGatewayUrl(paymentGatewayUrl);

        paymentRequest = createPaymentRequest();
        HttpHeaders expectedHeaders = createExpectedHeaders();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(paymentRequest);
        requestEntity = new HttpEntity<>(jsonString, expectedHeaders);
    }

    @Test
    public void testChargeCreditCard_FailedPayment() {
        // Arrange
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.exchange(eq(paymentGatewayUrl), eq(HttpMethod.POST), eq(requestEntity), eq(String.class))).thenReturn(responseEntity);

        // Act & Assert
        assertThrows(PaymentGatewayException.class, () -> paymentGatewayClient.chargeCreditCard(paymentRequest));
    }

    @Test
    public void testChargeCreditCard_NetworkError() {
        // Arrange
        when(restTemplate.exchange(eq(paymentGatewayUrl), eq(HttpMethod.POST), eq(requestEntity), eq(String.class))).thenThrow(new RuntimeException("Network Error"));

        // Act & Assert
        assertThrows(PaymentGatewayException.class, () -> paymentGatewayClient.chargeCreditCard(paymentRequest));
    }

    private PaymentGatewayRequest createPaymentRequest() {
        PaymentGatewayRequest paymentRequest = new PaymentGatewayRequest();
        paymentRequest.setCardHolderName("Stuart Clark");
        paymentRequest.setCardNumber("4111111111111111");
        paymentRequest.setCardCvv("123");
        paymentRequest.setAmount(new BigDecimal("10.0"));
        paymentRequest.setCardExpiryMonth(TEST_CARD_EXPIRY_MONTH);
        paymentRequest.setCardExpiryYear(TEST_CARD_EXPIRY_YEAR);
        paymentRequest.setCurrency("USD");
        return paymentRequest;
    }

    private HttpHeaders createExpectedHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private PaymentResponse createExpectedResponse() {
        PaymentResponse response = new PaymentResponse();
        response.setStatus("Completed");
        response.setMessage("Amount Received");
        return response;
    }
}

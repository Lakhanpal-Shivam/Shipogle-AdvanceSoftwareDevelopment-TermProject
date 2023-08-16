package com.shipogle.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipogle.app.TestConstants;
import com.shipogle.app.model.PaymentGatewayRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class PaymentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    final private int TEST_CARD_EXPIRY_MONTH=12;
    final private int TEST_CARD_EXPIRY_YEAR=2025;

    @Test
    public void testChargeCreditCard() throws Exception {
        String endpoint = "/payment/charge";
        PaymentGatewayRequest paymentRequest = new PaymentGatewayRequest();
        paymentRequest.setAmount(new BigDecimal("189.00"));
        paymentRequest.setCurrency("USD");
        paymentRequest.setCardNumber("4112323111111111");
        paymentRequest.setCardExpiryMonth(TEST_CARD_EXPIRY_MONTH);
        paymentRequest.setCardExpiryYear(TEST_CARD_EXPIRY_YEAR);
        paymentRequest.setCardCvv("567");
        paymentRequest.setCardHolderName("Stuart Clark");
        System.out.println(objectMapper.writeValueAsString(paymentRequest));

        mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TestConstants.TEST_TOKEN)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
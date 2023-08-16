package com.shipogle.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipogle.app.exception.PaymentGatewayException;
import com.shipogle.app.model.PaymentGatewayRequest;
import com.shipogle.app.model.PaymentResponse;
import com.shipogle.app.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PaymentControllerTest {

    @Test
    void testChargeCreditCard() throws JsonProcessingException, PaymentGatewayException {
        // Setup
        PaymentGatewayRequest paymentRequest = new PaymentGatewayRequest();
        PaymentResponse paymentResponse = new PaymentResponse();

        PaymentService paymentServiceMock = Mockito.mock(PaymentService.class);
        when(paymentServiceMock.chargeCreditCard(Mockito.any(PaymentGatewayRequest.class))).thenReturn(paymentResponse);

        ObjectMapper objectMapperMock = Mockito.mock(ObjectMapper.class);
        when(objectMapperMock.readValue(anyString(), eq(PaymentGatewayRequest.class))).thenReturn(paymentRequest);

        PaymentController controller = new PaymentController();
        controller.setPaymentService(paymentServiceMock);

        // Exercise
        String jsonString = "{}";
        ResponseEntity<?> responseEntity = controller.chargeCreditCard(jsonString);

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(paymentResponse, responseEntity.getBody());
    }

    @Test
    void testChargeCreditCardWithInvalidJson() throws JsonProcessingException {
        // Setup
        ObjectMapper objectMapperMock = Mockito.mock(ObjectMapper.class);
        when(objectMapperMock.readValue(anyString(), eq(PaymentGatewayRequest.class))).thenThrow(PaymentGatewayException.class);

        PaymentController controller = new PaymentController();

        // Exercise
        String jsonString = "";
        ResponseEntity<?> responseEntity = controller.chargeCreditCard(jsonString);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testChargeCreditCardWithPaymentGatewayException() throws JsonProcessingException, PaymentGatewayException {
        // Setup
        PaymentGatewayRequest paymentRequest = new PaymentGatewayRequest();

        PaymentService paymentServiceMock = Mockito.mock(PaymentService.class);
        when(paymentServiceMock.chargeCreditCard(paymentRequest)).thenThrow(PaymentGatewayException.class);

        ObjectMapper objectMapperMock = Mockito.mock(ObjectMapper.class);
        when(objectMapperMock.readValue(anyString(), eq(PaymentGatewayRequest.class))).thenReturn(paymentRequest);

        PaymentController controller = new PaymentController();
        controller.setPaymentService(paymentServiceMock);

        // Exercise
        String jsonString = "{InvalidJson}";
        ResponseEntity<?> responseEntity = controller.chargeCreditCard(jsonString);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}


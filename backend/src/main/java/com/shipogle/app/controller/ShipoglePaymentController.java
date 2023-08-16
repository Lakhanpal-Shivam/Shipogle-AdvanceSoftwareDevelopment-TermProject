package com.shipogle.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipogle.app.model.PaymentGatewayRequest;
import com.shipogle.app.model.PaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ShipoglePaymentController{

    /**
     * Charge credit card.
     *
     * @author Shivam Lakhanpal
     * @param jsonString json string.
     * @return response entity.
     */
    @PostMapping("/ShipoglePay")
    public ResponseEntity<PaymentResponse> chargeCreditCard(@RequestBody String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PaymentGatewayRequest paymentRequest = objectMapper.readValue(jsonString, PaymentGatewayRequest.class);

            PaymentResponse response = new PaymentResponse();
            response.setStatus("Completed");
            response.setMessage("Amount Received");
            response.setTransactionId(UUID.randomUUID().toString());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
package com.shipogle.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipogle.app.exception.PaymentGatewayException;
import com.shipogle.app.model.PaymentGatewayRequest;
import com.shipogle.app.model.PaymentResponse;
import com.shipogle.app.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {


    private PaymentService paymentService;

    /**
     * Set payment service.
     *
     * @author Shivam Lakhanpal
     * @param paymentService payment service.
     */
    @Autowired
    public void setPaymentService(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    /**
     * Charge credit card.
     *
     * @author Shivam Lakhanpal
     * @param jsonString json string.
     * @return response entity.
     */
    @PostMapping("/charge")
    public ResponseEntity<?> chargeCreditCard(@RequestBody String jsonString) {
        try {
            if(jsonString.equals(""))
                throw new PaymentGatewayException("Invalid Card Details");

            ObjectMapper objectMapper = new ObjectMapper();
            PaymentGatewayRequest paymentRequest = objectMapper.readValue(jsonString, PaymentGatewayRequest.class);

            PaymentResponse paymentResponse = paymentService.chargeCreditCard(paymentRequest);
            if(paymentResponse == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment Failed");

            return ResponseEntity.ok(paymentResponse);
        } catch (PaymentGatewayException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}



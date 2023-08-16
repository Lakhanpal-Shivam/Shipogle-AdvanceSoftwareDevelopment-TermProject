package com.shipogle.app.exception;

public class PaymentGatewayException extends RuntimeException {

    /**
     * Constructor for PaymentGatewayException.
     *
     * @author Shivam Lakhanpal
     * @param message message.
     */
    public PaymentGatewayException(String message) {
        super(message);
    }
}


package com.ramcharans.chipotle.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentNotFoundException extends Exception {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}

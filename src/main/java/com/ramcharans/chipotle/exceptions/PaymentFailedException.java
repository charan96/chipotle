package com.ramcharans.chipotle.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentFailedException extends Exception {
    public PaymentFailedException(String message) {
        super(message);
    }
}

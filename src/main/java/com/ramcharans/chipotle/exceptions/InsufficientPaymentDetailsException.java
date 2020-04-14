package com.ramcharans.chipotle.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InsufficientPaymentDetailsException extends Exception {
    public InsufficientPaymentDetailsException(String message) {
        super(message);
    }
}

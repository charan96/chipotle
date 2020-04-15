package com.ramcharans.chipotle.transaction.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidPaymentDetailsException extends Exception {
    public InvalidPaymentDetailsException(String message) {
        super(message);
    }
}

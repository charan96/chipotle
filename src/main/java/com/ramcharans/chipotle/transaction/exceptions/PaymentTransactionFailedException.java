package com.ramcharans.chipotle.transaction.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentTransactionFailedException extends Exception {
    public PaymentTransactionFailedException(String message) {
        super(message);
    }
}

package com.ramcharans.chipotle.order.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(String message) {
        super(message);
    }
}

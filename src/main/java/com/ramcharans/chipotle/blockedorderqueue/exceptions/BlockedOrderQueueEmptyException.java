package com.ramcharans.chipotle.blockedorderqueue.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BlockedOrderQueueEmptyException extends Exception {
    public BlockedOrderQueueEmptyException(String message) {
        super(message);
    }
}

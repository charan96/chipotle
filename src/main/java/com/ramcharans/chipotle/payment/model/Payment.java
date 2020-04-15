package com.ramcharans.chipotle.payment.model;

import lombok.Data;

@Data
public class Payment {
    public static enum Type {CREDIT_CARD, CASH, CHECK, GIFT_CARD}

    private String id;
    private Long orderId;

    private Type type;
    private Double amount;
    private Boolean isSuccess;
}

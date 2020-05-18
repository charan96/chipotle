package com.ramcharans.chipotle.payment.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "payment")
public class Payment {
    public static enum Type {CREDIT_CARD, CASH, CHECK, GIFT_CARD}

    @Id
    private String id;
    private String orderId;

    private Type type;
    private Double amount;
    private Boolean isSuccess;
}

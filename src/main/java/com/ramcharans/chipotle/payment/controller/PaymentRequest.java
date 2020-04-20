package com.ramcharans.chipotle.payment.controller;

import com.ramcharans.chipotle.payment.model.Payment;
import lombok.Data;

import java.util.Map;

@Data
public class PaymentRequest {
    private String orderId;
    private Payment.Type paymentType;
    private Map<String, String> paymentDetails;
}

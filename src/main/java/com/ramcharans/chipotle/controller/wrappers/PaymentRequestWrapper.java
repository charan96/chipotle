package com.ramcharans.chipotle.controller.wrappers;

import com.ramcharans.chipotle.model.Order;
import com.ramcharans.chipotle.model.Payment.Type;
import lombok.Data;

import java.util.Map;

@Data
public class PaymentRequestWrapper {
    Order order;
    Type paymentType;
    Map<String, String> paymentDetails;
}

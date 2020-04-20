package com.ramcharans.chipotle.payment.controller;

import com.ramcharans.chipotle.payment.model.Payment;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
public class PaymentRequest {
    @ApiModelProperty(value = "order ID")
    private String orderId;

    @ApiModelProperty(value = "type of Payment")
    private Payment.Type paymentType;

    @ApiModelProperty(value = "payment details related to payment type")
    private Map<String, String> paymentDetails;
}

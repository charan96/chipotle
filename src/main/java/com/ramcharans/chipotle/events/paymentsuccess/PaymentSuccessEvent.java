package com.ramcharans.chipotle.events.paymentsuccess;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ramcharans.chipotle.payment.model.Payment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PaymentSuccessEvent {
    /* NOTE: events should contain information on when and who created the event; currently, we're ignoring who */

    // NOTE: the JSON Format is required for deserialization support for LocalDateTime so Jackson can deserialize it
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime timestamp;
    private Payment payment;

    public PaymentSuccessEvent(Payment payment) {
        timestamp = LocalDateTime.now();
        this.payment = payment;
    }
}

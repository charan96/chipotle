package com.ramcharans.chipotle.events.paymentsuccess;

import com.ramcharans.chipotle.RabbitConfig;
import com.ramcharans.chipotle.payment.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class PaymentSuccessEventProducer {
    private final String routingKey;
    private final String exchange;
    private final RabbitTemplate rabbitTemplate;

    private static final Logger log = LoggerFactory.getLogger(PaymentSuccessEventProducer.class);

    public PaymentSuccessEventProducer(RabbitConfig rabbitConfig, RabbitTemplate rabbitTemplate) {
        routingKey = rabbitConfig.paymentSuccessRoutingKey;
        exchange = rabbitConfig.exchange;

        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(Payment payment) {
        PaymentSuccessEvent event = new PaymentSuccessEvent(payment);
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        log.info(MessageFormat.format("Payment Success event sent: {0}", event));
    }
}

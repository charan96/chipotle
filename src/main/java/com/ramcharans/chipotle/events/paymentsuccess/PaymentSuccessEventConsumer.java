package com.ramcharans.chipotle.events.paymentsuccess;

import com.ramcharans.chipotle.chef.service.ChefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class PaymentSuccessEventConsumer {
    private final ChefService chefService;

    private static final Logger log = LoggerFactory.getLogger(PaymentSuccessEventConsumer.class);

    public PaymentSuccessEventConsumer(ChefService chefService) {
        this.chefService = chefService;
    }

    @RabbitListener(queues = "${PAYMENT_SUCCESS_QUEUE}")
    public void receive(PaymentSuccessEvent event) {
        log.info(MessageFormat.format("received event message: {0}", event));
        chefService.prepareMealFromOrder(event.getPayment().getOrderId());
    }
}

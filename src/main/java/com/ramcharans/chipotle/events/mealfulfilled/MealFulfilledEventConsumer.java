package com.ramcharans.chipotle.events.mealfulfilled;

import com.ramcharans.chipotle.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class MealFulfilledEventConsumer {
    private final OrderService orderService;

    private static final Logger log = LoggerFactory.getLogger(MealFulfilledEventConsumer.class);

    public MealFulfilledEventConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = "${MEAL_FULFILLED_QUEUE}")
    public void receive(MealFulfilledEvent event) {
        log.info(MessageFormat.format("received event message: {0}", event));
        orderService.markOrderAsFulfilled(event.getOrderId());
    }
}

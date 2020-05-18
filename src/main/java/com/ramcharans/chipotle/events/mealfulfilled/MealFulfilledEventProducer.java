package com.ramcharans.chipotle.events.mealfulfilled;

import com.ramcharans.chipotle.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class MealFulfilledEventProducer {
    private final String routingKey;
    private final String exchange;
    private final RabbitTemplate rabbitTemplate;

    private static final Logger log = LoggerFactory.getLogger(MealFulfilledEventProducer.class);

    public MealFulfilledEventProducer(RabbitConfig rabbitConfig, RabbitTemplate rabbitTemplate) {
        routingKey = rabbitConfig.mealFulfilledRoutingKey;
        exchange = rabbitConfig.exchange;

        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(String orderId) {
        MealFulfilledEvent event = new MealFulfilledEvent(orderId);
        rabbitTemplate.convertAndSend(exchange, routingKey, event);

        log.info(MessageFormat.format("Meal fulfilled event sent: {0}", event));
    }
}

package com.ramcharans.chipotle.events.preppedingredientreplenished;

import com.ramcharans.chipotle.RabbitConfig;
import com.ramcharans.chipotle.preppedingredient.model.PreppedIngredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class PreppedIngredientReplenishedEventProducer {
    private final String routingKey;
    private final String exchange;
    private final RabbitTemplate rabbitTemplate;
    
    private static final Logger log = LoggerFactory.getLogger(PreppedIngredientReplenishedEventProducer.class);
    
    public PreppedIngredientReplenishedEventProducer(RabbitConfig rabbitConfig, RabbitTemplate rabbitTemplate) {
        routingKey = rabbitConfig.preppedIngredientReplenishedRoutingKey;
        exchange = rabbitConfig.exchange;
        
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public void send(PreppedIngredient preppedIngredient) {
        PreppedIngredientReplenishedEvent event = new PreppedIngredientReplenishedEvent(preppedIngredient);
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        
        log.info(MessageFormat.format("Prepped Ingredient replenished event sent: {0}", event));
    }
}

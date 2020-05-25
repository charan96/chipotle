package com.ramcharans.chipotle.events.preppedingredientstockatthreshold;

import com.ramcharans.chipotle.RabbitConfig;
import com.ramcharans.chipotle.preppedingredient.model.PreppedIngredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class PreppedIngredientStockAtThresholdEventProducer {
    private final String routingKey;
    private final String exchange;
    private final RabbitTemplate rabbitTemplate;
    
    private static final Logger log = LoggerFactory.getLogger(PreppedIngredientStockAtThresholdEventProducer.class);
    
    public PreppedIngredientStockAtThresholdEventProducer(RabbitConfig rabbitConfig, RabbitTemplate rabbitTemplate) {
        routingKey = rabbitConfig.preppedIngredientStockAtThresholdRoutingKey;
        exchange = rabbitConfig.exchange;
        
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public void send(PreppedIngredient preppedIngredient) {
        PreppedIngredientStockAtThresholdEvent event = new PreppedIngredientStockAtThresholdEvent(preppedIngredient);
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        log.info(MessageFormat.format("Prepped Ingredient Stock at Threshold event sent: {0}", event));
    }
}

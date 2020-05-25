package com.ramcharans.chipotle.events.preppedingredientstockatthreshold;

import com.ramcharans.chipotle.souschef.service.SousChefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class PreppedIngredientStockAtThresholdEventConsumer {
    private final SousChefService sousChefService;
    
    private static final Logger log = LoggerFactory.getLogger(PreppedIngredientStockAtThresholdEventConsumer.class);
    
    public PreppedIngredientStockAtThresholdEventConsumer(SousChefService sousChefService) {
        this.sousChefService = sousChefService;
    }
    
    @RabbitListener(queues = "${PREPPED_INGREDIENT_STOCK_AT_THRESHOLD_QUEUE}")
    public void receive(PreppedIngredientStockAtThresholdEvent event) {
        log.info(MessageFormat.format("received event message: {0}", event));
        sousChefService.preparePreppedIngredient(event.getPreppedIngredient());
    }
}

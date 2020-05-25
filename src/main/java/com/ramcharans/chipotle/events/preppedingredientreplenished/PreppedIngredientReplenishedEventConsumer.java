package com.ramcharans.chipotle.events.preppedingredientreplenished;

import com.ramcharans.chipotle.chef.service.ChefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class PreppedIngredientReplenishedEventConsumer {
    private ChefService chefService;
    
    private static final Logger log = LoggerFactory.getLogger(PreppedIngredientReplenishedEventConsumer.class);
    
    public PreppedIngredientReplenishedEventConsumer(ChefService chefService) {
        this.chefService = chefService;
    }
    
    @RabbitListener(queues = "${PREPPED_INGREDIENT_REPLENISHED_QUEUE}")
    public void receive(PreppedIngredientReplenishedEvent event) {
        log.info(MessageFormat.format("received event message: {0}", event));
        chefService.prepareMealFromBlockedOrderQueue();
    }
}

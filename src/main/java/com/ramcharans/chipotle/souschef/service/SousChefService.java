package com.ramcharans.chipotle.souschef.service;

import com.ramcharans.chipotle.events.preppedingredientreplenished.PreppedIngredientReplenishedEventProducer;
import com.ramcharans.chipotle.mealingredient.service.MealIngredientService;
import com.ramcharans.chipotle.preppedingredient.model.PreppedIngredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static java.lang.Thread.sleep;

@Service
public class SousChefService {
    private MealIngredientService mealIngredientService;
    
    private PreppedIngredientReplenishedEventProducer preppedIngredientReplenishedEventProducer;
    
    private static final Logger log = LoggerFactory.getLogger(SousChefService.class);
    
    public SousChefService(MealIngredientService mealIngredientService,
                           PreppedIngredientReplenishedEventProducer preppedIngredientReplenishedEventProducer) {
        this.mealIngredientService = mealIngredientService;
        this.preppedIngredientReplenishedEventProducer = preppedIngredientReplenishedEventProducer;
    }
    
    public void preparePreppedIngredient(PreppedIngredient preppedIngredient) {
        try {
            sleep(25000);
            mealIngredientService.replenishIngredientStock(preppedIngredient);
            firePreppedIngredientStockReplenished(preppedIngredient);
        } catch (InterruptedException e) {
            throw new RuntimeException("failed to prepare prepped ingredient: " + preppedIngredient.getName());
        }
    }
    
    private void firePreppedIngredientStockReplenished(PreppedIngredient preppedIngredient) {
        preppedIngredientReplenishedEventProducer.send(preppedIngredient);
    }
}

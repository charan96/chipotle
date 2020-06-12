package com.ramcharans.chipotle.mealingredient.service;

import com.ramcharans.chipotle.events.preppedingredientstockatthreshold.PreppedIngredientStockAtThresholdEventProducer;
import com.ramcharans.chipotle.preppedingredient.model.PreppedIngredient;
import com.ramcharans.chipotle.preppedingredient.service.PreppedIngredientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MealIngredientService {
    private final PreppedIngredientService preppedIngredientService;
    
    private final PreppedIngredientStockAtThresholdEventProducer preppedIngredientStockAtThresholdEventProducer;
    
    private static final Logger log = LoggerFactory.getLogger(MealIngredientService.class);
    
    public MealIngredientService(PreppedIngredientService preppedIngredientService,
                                 PreppedIngredientStockAtThresholdEventProducer preppedIngredientStockAtThresholdEventProducer) {
        this.preppedIngredientService = preppedIngredientService;
        this.preppedIngredientStockAtThresholdEventProducer = preppedIngredientStockAtThresholdEventProducer;
    }
    
    public List<PreppedIngredient> getAvailableIngredients() {
        return preppedIngredientService.getAllPreppedIngredients()
                .stream()
                .filter(ing -> ing.getStock() > 0)
                .collect(Collectors.toList());
    }
    
    public Optional<PreppedIngredient> findIngredientById(String id) {
        return preppedIngredientService.findPreppedIngredientById(id);
    }
    
    public List<PreppedIngredient> findIngredientsByType(PreppedIngredient.Type type) {
        return preppedIngredientService.findPreppedIngredientsByType(type);
    }
    
    public boolean isAllMealIngredientsAvailable(List<PreppedIngredient> ingredients) {
        for (PreppedIngredient ing : ingredients) {
            if (preppedIngredientService.isIngredientStockEmpty(ing))
                return false;
        }
        return true;
    }
    
    public void subtractFromPreppedIngredientsStock(List<PreppedIngredient> ingredients) {
        for (PreppedIngredient ing : ingredients) {
            preppedIngredientService.subtract1FromPreppedIngredientsStock(ing);
            
            if (preppedIngredientService.isIngredientStockAtThreshold(ing))
                firePreppedIngredientAtThresholdEvent(ing);
        }
    }
    
    public void firePreppedIngredientAtThresholdEvent(PreppedIngredient ing) {
        preppedIngredientStockAtThresholdEventProducer.send(ing);
    }
    
    public void replenishIngredientStock(PreppedIngredient preppedIngredient) {
        preppedIngredientService.updatePreppedIngredientStockLevel(preppedIngredient, preppedIngredient.getCapacity());
    }
}

package com.ramcharans.chipotle.mealingredient.service;

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
    PreppedIngredientService preppedIngredientService;
    
    private static final Logger log = LoggerFactory.getLogger(MealIngredientService.class);
    
    public MealIngredientService(PreppedIngredientService preppedIngredientService) {
        this.preppedIngredientService = preppedIngredientService;
    }
    
    public List<PreppedIngredient> getAvailableIngredients()
    {
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
}

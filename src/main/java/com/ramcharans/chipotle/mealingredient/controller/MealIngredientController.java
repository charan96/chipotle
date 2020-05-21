package com.ramcharans.chipotle.mealingredient.controller;

import com.ramcharans.chipotle.mealingredient.service.MealIngredientService;
import com.ramcharans.chipotle.preppedingredient.model.PreppedIngredient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Api(value = "Meal Ingredient Management System")
@RequestMapping(path = "/ingredients")
public class MealIngredientController {
    MealIngredientService mealIngredientService;
    
    private static final Logger log = LoggerFactory.getLogger(MealIngredientController.class);
    
    public MealIngredientController(MealIngredientService mealIngredientService) {
        this.mealIngredientService = mealIngredientService;
    }
    
    @ApiOperation(value = "Get all available ingredients", response = MealIngredientResponse.class, responseContainer =
            "List")
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Object> getAvailableIngredients() {
        log.info("retrieving available ingredients using IngredientService");
        
        List<PreppedIngredient> preppedIngredients = mealIngredientService.getAvailableIngredients();
        
        return new ResponseEntity<>(preppedIngredients
                .stream()
                .map(MealIngredientResponse::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }
    
    @ApiOperation(value = "find ingredient by ID", response = MealIngredientResponse.class)
    @GetMapping(value = "/filter/id/{id}", produces = "application/json")
    public ResponseEntity<Object> findIngredientById(@PathVariable String id) {
        Optional<PreppedIngredient> ingredient = mealIngredientService.findIngredientById(id);
        
        if (ingredient.isPresent()) {
            return new ResponseEntity<>(new MealIngredientResponse(ingredient.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("no ingredient found with given ID", HttpStatus.NOT_FOUND);
        }
    }
    
    @ApiOperation(value = "find ingredient by Type", response = MealIngredientResponse.class, responseContainer =
            "List")
    @GetMapping(value = "/filter/type/{type}", produces = "application/json")
    public ResponseEntity<Object> findIngredientsByType(@PathVariable PreppedIngredient.Type type) {
        List<PreppedIngredient> type_filtered_ingredients = mealIngredientService.findIngredientsByType(type);
        
        if (type_filtered_ingredients.isEmpty()) {
            return new ResponseEntity<>("no ingredients found with given Type", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(type_filtered_ingredients
                    .stream()
                    .map(MealIngredientResponse::new)
                    .collect(Collectors.toList()), HttpStatus.OK);
        }
    }
}

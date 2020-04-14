package com.ramcharans.chipotle.controller;

import com.ramcharans.chipotle.model.Ingredient;
import com.ramcharans.chipotle.model.Ingredient.Type;
import com.ramcharans.chipotle.service.IngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/ingredients")
public class IngredientController {
    @Autowired
    IngredientsService ingredientsService;

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<List<Ingredient>> getAvailableIngredients() {
        return new ResponseEntity<>(ingredientsService.getAvailableIngredients(), HttpStatus.OK);
    }

    @PostMapping(value = "/addIngredient", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> addIngredient(@RequestBody Ingredient ingredient) {
        ingredientsService.addIngredient(ingredient);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/addIngredients", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> addIngredients(@RequestBody List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients)
            ingredientsService.addIngredient(ingredient);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/filter/id/{id}", produces = "application/json")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable Long id) {
        Optional<Ingredient> ingredient = ingredientsService.getIngredientById(id);

        if (ingredient.isPresent())
            return new ResponseEntity<>(ingredient.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/filter/type/{type}", produces = "application/json")
    public ResponseEntity<List<Ingredient>> getIngredientsByType(@PathVariable Type type) {
        List<Ingredient> type_filtered_ingredients = ingredientsService.getIngredientsByType(type);

        if (type_filtered_ingredients.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(type_filtered_ingredients, HttpStatus.OK);
    }
}

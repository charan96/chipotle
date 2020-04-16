package com.ramcharans.chipotle.ingredient.controller;

import com.ramcharans.chipotle.ingredient.exceptions.FailedToAddIngredientException;
import com.ramcharans.chipotle.ingredient.exceptions.IngredientAlreadyExistsException;
import com.ramcharans.chipotle.ingredient.model.Ingredient;
import com.ramcharans.chipotle.ingredient.model.Ingredient.Type;
import com.ramcharans.chipotle.ingredient.service.IngredientsService;
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
    public ResponseEntity<Object> getAvailableIngredients() {
        return new ResponseEntity<>(ingredientsService.getAvailableIngredients(), HttpStatus.OK);
    }

    @PostMapping(value = "/addIngredient", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addIngredient(@RequestBody Ingredient ingredient) {
        try {
            ingredientsService.addIngredient(ingredient);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToAddIngredientException e) {
            return new ResponseEntity<>("failed to add ingredient", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IngredientAlreadyExistsException e) {
            return new ResponseEntity<>("ingredient already exists", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/addIngredients", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addIngredients(@RequestBody List<Ingredient> ingredients) {
        try {
            for (Ingredient ingredient : ingredients)
                ingredientsService.addIngredient(ingredient);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToAddIngredientException e) {
            return new ResponseEntity<>("failed to add ingredients", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IngredientAlreadyExistsException e) {
            return new ResponseEntity<>("ingredient already exists", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/filter/id/{id}", produces = "application/json")
    public ResponseEntity<Object> getIngredientById(@PathVariable Long id) {
        Optional<Ingredient> ingredient = ingredientsService.getIngredientById(id);

        if (ingredient.isPresent())
            return new ResponseEntity<>(ingredient.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>("no ingredient found with given ID", HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/filter/type/{type}", produces = "application/json")
    public ResponseEntity<Object> getIngredientsByType(@PathVariable Type type) {
        List<Ingredient> type_filtered_ingredients = ingredientsService.getIngredientsByType(type);

        if (type_filtered_ingredients.isEmpty())
            return new ResponseEntity<>("no ingredients found with given Type", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(type_filtered_ingredients, HttpStatus.OK);
    }
}
package com.ramcharans.chipotle.ingredient.controller;

import com.ramcharans.chipotle.ingredient.exceptions.FailedToAddIngredientException;
import com.ramcharans.chipotle.ingredient.exceptions.IngredientAlreadyExistsException;
import com.ramcharans.chipotle.ingredient.exceptions.IngredientNotFoundException;
import com.ramcharans.chipotle.ingredient.model.Ingredient;
import com.ramcharans.chipotle.ingredient.model.Ingredient.Type;
import com.ramcharans.chipotle.ingredient.service.IngredientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Api(value = "Ingredient Management System")
@RequestMapping(path = "/ingredients")
public class IngredientController {
    @Autowired
    IngredientService ingredientService;

    private static final Logger log = LoggerFactory.getLogger(IngredientController.class);

    @ApiOperation(value = "Get all available ingredients", response = Ingredient.class, responseContainer = "List")
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Object> getAvailableIngredients() {
        log.info("retrieving available ingredients using IngredientService");
        return new ResponseEntity<>(ingredientService.getAvailableIngredients(), HttpStatus.OK);
    }

    @ApiOperation(value = "add 1 ingredient", response = String.class)
    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    // TODO: change ingredient to ingredient Request
    public ResponseEntity<Object> addIngredient(@RequestBody Ingredient ingredient) {
        try {
            String ingredientId = ingredientService.addIngredient(ingredient);
            log.info("successfully added Ingredient: {}", ingredient);
            return new ResponseEntity<>(ingredientId, HttpStatus.OK);
        } catch (IngredientAlreadyExistsException e) {
            log.info("ingredient already exists: {}", ingredient);
            return new ResponseEntity<>("ingredient already exists", HttpStatus.BAD_REQUEST);
        } catch (FailedToAddIngredientException e) {
            log.info("failed to add ingredient: {}", ingredient);
            return new ResponseEntity<>("failed to add ingredient: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "add mulitple ingredients")
    @PostMapping(value = "/addMany", consumes = "application/json", produces = "application/json")
    // TODO: change List<Ingredient> to List<IngredientRequest>
    public ResponseEntity<Object> addIngredients(@RequestBody List<Ingredient> ingredients) {
        try {
            ingredientService.addAllIngredients(ingredients);
            log.info("successfully added ingredients: {}", ingredients);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToAddIngredientException e) {
            log.info("failed to add ingrediens: {}", ingredients);
            return new ResponseEntity<>("failed to add ingredients", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IngredientAlreadyExistsException e) {
            log.info("An ingredient from the following ingredients already exists: {}", ingredients);
            return new ResponseEntity<>("ingredient already exists", HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "find ingredient by ID", response = Ingredient.class)
    @GetMapping(value = "/filter/id/{id}", produces = "application/json")
    public ResponseEntity<Object> findIngredientById(@PathVariable String id) {
        Optional<Ingredient> ingredient = ingredientService.getIngredientById(id);

        if (ingredient.isPresent()) {
            log.info("successfully found Ingredient with ID: {}", id);
            return new ResponseEntity<>(ingredient.get(), HttpStatus.OK);
        } else {
            log.info("no ingredient found with ID: {}", id);
            return new ResponseEntity<>("no ingredient found with given ID", HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "find ingredient by Type", response = Ingredient.class, responseContainer = "List")
    @GetMapping(value = "/filter/type/{type}", produces = "application/json")
    public ResponseEntity<Object> findIngredientsByType(@PathVariable Type type) {
        List<Ingredient> type_filtered_ingredients = ingredientService.getIngredientsByType(type);

        if (type_filtered_ingredients.isEmpty()) {
            log.info("no ingredients were found with type: {}", type);
            return new ResponseEntity<>("no ingredients found with given Type", HttpStatus.NOT_FOUND);
        } else {
            log.info("following ingredients were found of type '{}': {}", type, type_filtered_ingredients);
            return new ResponseEntity<>(type_filtered_ingredients, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "delete ingredient by ID")
    @DeleteMapping(value = "/delete/id/{id}", produces = "application/json")
    public ResponseEntity<Object> deleteIngredientById(@PathVariable String id) {
        try {
            ingredientService.deleteIngredient(id);
            log.info("successfully deleted ID with type: {}", id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IngredientNotFoundException e) {
            log.info("no ingredient found with ID '{}' to delete", id);
            return new ResponseEntity<>("ingredient not found", HttpStatus.NOT_FOUND);
        }
    }
}

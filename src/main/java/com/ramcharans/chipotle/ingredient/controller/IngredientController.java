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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// TODO: add validation to all requests and inputs
// TODO: look at debug of DAO for proxy class impl. by Spring and change DAO to MongoRepository extension
// TODO: add logging
// TODO: make swagger more documented if possible
// TODO: add messaging impl. with this and Chef application (Kafka vs. RMQ)

@RestController
@Api(value = "Ingredient Management System")
@RequestMapping(path = "/ingredients")
public class IngredientController {
    IngredientService ingredientService;

    private static final Logger log = LoggerFactory.getLogger(IngredientController.class);

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @ApiOperation(value = "Get all available ingredients", response = Ingredient.class, responseContainer = "List")
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Object> getAvailableIngredients() {
        log.info("retrieving available ingredients using IngredientService");
        return new ResponseEntity<>(ingredientService.getAvailableIngredients(), HttpStatus.OK);
    }

    @ApiOperation(value = "add 1 ingredient", response = String.class)
    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addIngredient(@RequestBody IngredientRequest ingredientRequest) {
        try {
            String ingredientId = ingredientService.addIngredientFromIngredientRequest(ingredientRequest);
            return new ResponseEntity<>(ingredientId, HttpStatus.OK);
        } catch (IngredientAlreadyExistsException e) {
            return new ResponseEntity<>("ingredient already exists", HttpStatus.BAD_REQUEST);
        } catch (FailedToAddIngredientException e) {
            return new ResponseEntity<>("failed to add ingredient: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "add multiple ingredients")
    @PostMapping(value = "/addMany", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addIngredients(@RequestBody List<IngredientRequest> ingredientRequests) {
        try {
            ingredientService.addMultipleIngredientsFromIngredientRequestList(ingredientRequests);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToAddIngredientException e) {
            return new ResponseEntity<>("failed to add ingredients", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IngredientAlreadyExistsException e) {
            return new ResponseEntity<>("ingredient already exists", HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "find ingredient by ID", response = Ingredient.class)
    @GetMapping(value = "/filter/id/{id}", produces = "application/json")
    public ResponseEntity<Object> findIngredientById(@PathVariable String id) {
        Optional<Ingredient> ingredient = ingredientService.getIngredientById(id);

        if (ingredient.isPresent()) {
            return new ResponseEntity<>(ingredient.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("no ingredient found with given ID", HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "find ingredient by Type", response = Ingredient.class, responseContainer = "List")
    @GetMapping(value = "/filter/type/{type}", produces = "application/json")
    public ResponseEntity<Object> findIngredientsByType(@PathVariable Type type) {
        List<Ingredient> type_filtered_ingredients = ingredientService.getIngredientsByType(type);

        if (type_filtered_ingredients.isEmpty()) {
            return new ResponseEntity<>("no ingredients found with given Type", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(type_filtered_ingredients, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "delete ingredient by ID")
    @DeleteMapping(value = "/delete/id/{id}", produces = "application/json")
    public ResponseEntity<Object> deleteIngredientById(@PathVariable String id) {
        try {
            ingredientService.deleteIngredient(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IngredientNotFoundException e) {
            return new ResponseEntity<>("ingredient not found", HttpStatus.NOT_FOUND);
        }
    }
}

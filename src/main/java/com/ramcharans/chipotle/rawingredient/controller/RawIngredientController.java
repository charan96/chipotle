package com.ramcharans.chipotle.rawingredient.controller;

import com.ramcharans.chipotle.rawingredient.exceptions.FailedToAddRawIngredientException;
import com.ramcharans.chipotle.rawingredient.exceptions.RawIngredientAlreadyExistsException;
import com.ramcharans.chipotle.rawingredient.exceptions.RawIngredientNotFoundException;
import com.ramcharans.chipotle.rawingredient.model.RawIngredient;
import com.ramcharans.chipotle.rawingredient.service.RawIngredientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Api(value = "Raw Ingredient Management Service")
@RequestMapping(path = "/raw_ingredients")
public class RawIngredientController {
    RawIngredientService rawIngredientService;
    
    private static final Logger log = LoggerFactory.getLogger(RawIngredientController.class);
    
    public RawIngredientController(RawIngredientService rawIngredientService) {
        this.rawIngredientService = rawIngredientService;
    }
    
    @ApiOperation(value = "Gets all available raw ingredients", response = RawIngredient.class, responseContainer =
            "List")
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Object> getAvailableRawIngredients() {
        return new ResponseEntity<>(rawIngredientService.getAllRawIngredients(), HttpStatus.OK);
    }
    
    @ApiOperation(value = "add 1 Raw Ingredient", response = String.class)
    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addRawIngredient(@RequestBody RawIngredientRequest rawIngredientRequest) {
        try {
            String id = rawIngredientService.addRawIngredientFromRawIngredientRequest(rawIngredientRequest);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (RawIngredientAlreadyExistsException e) {
            return new ResponseEntity<>("ingredient already exists with name", HttpStatus.BAD_REQUEST);
        } catch (FailedToAddRawIngredientException e) {
            return new ResponseEntity<>("failed to add ingredient", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @ApiOperation(value = "add multiple ingredients")
    @PostMapping(value = "/addMany", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addManyRawIngredients(@RequestBody List<RawIngredientRequest> ingredientRequests) {
        try {
            rawIngredientService.addMultipleRawIngredientsFromIngredientRequestList(ingredientRequests);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToAddRawIngredientException e) {
            return new ResponseEntity<>("failed to add ingredients", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RawIngredientAlreadyExistsException e) {
            return new ResponseEntity<>("ingredient already exists", HttpStatus.BAD_REQUEST);
        }
    }
    
    @ApiOperation(value = "find raw ingredient by ID", response = RawIngredient.class)
    @GetMapping(value = "/filter/id/{id}", produces = "application/json")
    public ResponseEntity<Object> findById(@PathVariable String id) {
        Optional<RawIngredient> ingredient = rawIngredientService.findRawIngredientById(id);
        
        if (ingredient.isPresent())
            return new ResponseEntity<>(ingredient.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>("no raw ingredient found with given ID", HttpStatus.BAD_REQUEST);
    }
    
    @ApiOperation(value = "find raw ingredient by name", response = RawIngredient.class)
    @GetMapping(value = "/filter/name/{name}", produces = "application/json")
    public ResponseEntity<Object> findByName(@PathVariable String name) {
        Optional<RawIngredient> ing = rawIngredientService.findRawIngredientByName(name);
        
        if (ing.isPresent())
            return new ResponseEntity<>(ing.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>("no raw ingredient with given name was found", HttpStatus.BAD_REQUEST);
    }
    
    @ApiOperation(value = "delete raw ingredient by ID")
    @DeleteMapping(value = "/delete/id/{id}", produces = "application/json")
    public ResponseEntity<Object> deleteById(@PathVariable String id) {
        try {
            rawIngredientService.deleteRawIngredientById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RawIngredientNotFoundException e) {
            return new ResponseEntity<>("no raw ingredient found with given ID", HttpStatus.BAD_REQUEST);
        }
    }
    
    @ApiOperation(value = "delete all raw ingredients")
    @DeleteMapping(value = "/delete/all", produces = "application/json")
    public ResponseEntity<Object> deleteAll() {
        rawIngredientService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

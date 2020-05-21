package com.ramcharans.chipotle.preppedingredient.controller;

import com.ramcharans.chipotle.preppedingredient.exceptions.FailedToAddPreppedIngredientException;
import com.ramcharans.chipotle.preppedingredient.exceptions.PreppedIngredientAlreadyExistsException;
import com.ramcharans.chipotle.preppedingredient.exceptions.PreppedIngredientNotFoundException;
import com.ramcharans.chipotle.preppedingredient.model.PreppedIngredient;
import com.ramcharans.chipotle.preppedingredient.service.PreppedIngredientService;
import com.ramcharans.chipotle.rawingredient.exceptions.RawIngredientNotFoundException;
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
@Api(value = "Prepped Ingredient Management Service")
@RequestMapping(path = "/prepped_ingredient")
public class PreppedIngredientController {
    PreppedIngredientService preppedIngredientService;
    
    private static final Logger log = LoggerFactory.getLogger(PreppedIngredientController.class);
    
    public PreppedIngredientController(PreppedIngredientService preppedIngredientService) {
        this.preppedIngredientService = preppedIngredientService;
    }
    
    @ApiOperation(value = "Gets all available prepped ingredients", response = PreppedIngredient.class,
            responseContainer = "List")
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Object> getAvailableIngredients() {
        return new ResponseEntity<>(preppedIngredientService.getAllPreppedIngredients(), HttpStatus.OK);
    }
    
    @ApiOperation(value = "add 1 Prepped Ingredient", response = String.class)
    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addPreppedIngredient(@RequestBody PreppedIngredientRequest preppedIngredientRequest) {
        try {
            String id =
                    preppedIngredientService.addPreppedIngredientFromPreppedIngredientRequest(preppedIngredientRequest);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (PreppedIngredientAlreadyExistsException e) {
            return new ResponseEntity<>("ingredient already exists with name", HttpStatus.BAD_REQUEST);
        } catch (FailedToAddPreppedIngredientException e) {
            return new ResponseEntity<>("failed to add ingredient", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RawIngredientNotFoundException e) {
            return new ResponseEntity<>("raw ingredient ID is invalid", HttpStatus.BAD_REQUEST);
        }
    }
    
    @ApiOperation(value = "add multiple ingredients")
    @PostMapping(value = "/addMany", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addMultiplePreppedIngredients(@RequestBody List<PreppedIngredientRequest> preppedIngredientRequests) {
        try {
            preppedIngredientService.addMultiplePreppedIngredientFromPreppedIngredientRequestList(preppedIngredientRequests);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToAddPreppedIngredientException e) {
            return new ResponseEntity<>("failed to add ingredients", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (PreppedIngredientAlreadyExistsException e) {
            return new ResponseEntity<>("ingredient already exists", HttpStatus.BAD_REQUEST);
        } catch (RawIngredientNotFoundException e) {
            return new ResponseEntity<>("raw ingredient ID is invalid", HttpStatus.BAD_REQUEST);
        }
    }
    
    @ApiOperation(value = "find prepped ingredient by ID", response = PreppedIngredient.class)
    @GetMapping(value = "/filter/id/{id}", produces = "application/json")
    public ResponseEntity<Object> findPreppedIngredientById(@PathVariable String id) {
        Optional<PreppedIngredient> ingredient = preppedIngredientService.findPreppedIngredientById(id);
        
        if (ingredient.isPresent())
            return new ResponseEntity<>(ingredient.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>("no prepped ingredient found with given ID", HttpStatus.BAD_REQUEST);
    }
    
    @ApiOperation(value = "find prepped ingredient by name", response = PreppedIngredient.class)
    @GetMapping(value = "/filter/name/{name}", produces = "application/json")
    public ResponseEntity<Object> findPreppedIngredientByName(@PathVariable String name) {
        Optional<PreppedIngredient> ingredient = preppedIngredientService.findPreppedIngredientByName(name);
        
        if (ingredient.isPresent())
            return new ResponseEntity<>(ingredient.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>("no prepped ingredient found with given ID", HttpStatus.BAD_REQUEST);
    }
    
    @ApiOperation(value = "find prepped ingredients by Type", response = PreppedIngredient.class, responseContainer =
            "List")
    @GetMapping(value = "/filter/type/{type}", produces = "application/json")
    public ResponseEntity<Object> findPreppedIngredientsByType(@PathVariable PreppedIngredient.Type type) {
        return new ResponseEntity<>(preppedIngredientService.findPreppedIngredientsByType(type), HttpStatus.OK);
    }
    
    @ApiOperation(value = "delete prepped ingredient by ID")
    @DeleteMapping(value = "/delete/id/{id}", produces = "application/json")
    public ResponseEntity<Object> deletePreppedIngredientById(@PathVariable String id) {
        try {
            preppedIngredientService.deletePreppedIngredientById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PreppedIngredientNotFoundException e) {
            return new ResponseEntity<>("no prepped ingredient found with given ID", HttpStatus.BAD_REQUEST);
        }
    }
    
    @ApiOperation(value = "find all prepped ingredients containing the given raw ingredient ID",
            response = PreppedIngredient.class, responseContainer = "List")
    @GetMapping(value = "/filter/raw_ingredient/{rawIngredientId}", produces = "application/json")
    public ResponseEntity<Object> findPreppedIngredientByRawIngredient(@PathVariable String rawIngredientId) {
        try {
            List<PreppedIngredient> preppedIngredients =
                    preppedIngredientService.findPreppedIngredientsByRawIngredient(rawIngredientId);
            return new ResponseEntity<>(preppedIngredients, HttpStatus.OK);
        } catch (RawIngredientNotFoundException e) {
            return new ResponseEntity<>("provided raw ingredient ID is invalid", HttpStatus.BAD_REQUEST);
        }
    }
}

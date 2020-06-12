package com.ramcharans.chipotle.preppedingredient.service;

import com.ramcharans.chipotle.preppedingredient.controller.PreppedIngredientRequest;
import com.ramcharans.chipotle.preppedingredient.dao.PreppedIngredientRepository;
import com.ramcharans.chipotle.preppedingredient.exceptions.FailedToAddPreppedIngredientException;
import com.ramcharans.chipotle.preppedingredient.exceptions.PreppedIngredientAlreadyExistsException;
import com.ramcharans.chipotle.preppedingredient.exceptions.PreppedIngredientNotFoundException;
import com.ramcharans.chipotle.preppedingredient.model.PreppedIngredient;
import com.ramcharans.chipotle.rawingredient.exceptions.RawIngredientNotFoundException;
import com.ramcharans.chipotle.rawingredient.model.RawIngredient;
import com.ramcharans.chipotle.rawingredient.service.RawIngredientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PreppedIngredientService {
    private static final Logger log = LoggerFactory.getLogger(PreppedIngredientService.class);
    
    RawIngredientService rawIngredientService;
    
    PreppedIngredientRepository preppedIngredientRepo;
    
    public PreppedIngredientService(RawIngredientService rawIngredientService,
                                    PreppedIngredientRepository preppedIngredientRepository) {
        this.rawIngredientService = rawIngredientService;
        this.preppedIngredientRepo = preppedIngredientRepository;
    }
    
    public String addPreppedIngredientFromPreppedIngredientRequest(PreppedIngredientRequest preppedIngredientRequest) throws
            PreppedIngredientAlreadyExistsException, FailedToAddPreppedIngredientException,
            RawIngredientNotFoundException {
        PreppedIngredient ingredient = convertPreppedIngredientToPreppedIngredient(preppedIngredientRequest);
        return safeAddPreppedIngredient(ingredient);
    }
    
    public void addMultiplePreppedIngredientFromPreppedIngredientRequestList(List<PreppedIngredientRequest> preppedIngredientRequestList) throws
            PreppedIngredientAlreadyExistsException, FailedToAddPreppedIngredientException,
            RawIngredientNotFoundException {
        List<PreppedIngredient> ingredients = new ArrayList<>();
        
        for (PreppedIngredientRequest preppedIngredientRequest : preppedIngredientRequestList) {
            PreppedIngredient ingredient = convertPreppedIngredientToPreppedIngredient(preppedIngredientRequest);
            ingredients.add(ingredient);
        }
        
        addMultiplePreppedIngredient(ingredients);
    }
    
    public List<PreppedIngredient> getAllPreppedIngredients() {
        return preppedIngredientRepo.findAll();
    }
    
    public Optional<PreppedIngredient> findPreppedIngredientById(String id) {
        return preppedIngredientRepo.findById(id);
    }
    
    public Optional<PreppedIngredient> findPreppedIngredientByName(String name) {
        return preppedIngredientRepo.findByName(name);
    }
    
    public void deletePreppedIngredientById(String id) throws PreppedIngredientNotFoundException {
        Optional<PreppedIngredient> ing = preppedIngredientRepo.findById(id);
        
        if (ing.isPresent())
            preppedIngredientRepo.deleteById(id);
        else
            throw new PreppedIngredientNotFoundException();
    }
    
    public List<PreppedIngredient> findPreppedIngredientsByType(PreppedIngredient.Type type) {
        return preppedIngredientRepo.findAllByType(type);
    }
    
    public List<PreppedIngredient> findPreppedIngredientsByRawIngredient(String rawIngredientId) throws RawIngredientNotFoundException {
        Optional<RawIngredient> rawIngredient = rawIngredientService.findRawIngredientById(rawIngredientId);
        
        if (rawIngredient.isPresent()) {
            return getAllPreppedIngredients()
                    .stream()
                    .filter(ing -> ing.getRawIngredientsNeeded().contains(rawIngredient.get()))
                    .collect(Collectors.toList());
        } else {
            throw new RawIngredientNotFoundException();
        }
    }
    
    public void deleteAll() {
        preppedIngredientRepo.deleteAll();
    }
    
    public boolean isIngredientStockAtThreshold(PreppedIngredient ingredient) {
        return ingredient.getStock().equals(computeCapacityThreshold(ingredient));
    }
    
    public boolean isIngredientStockEmpty(PreppedIngredient ingredient) {
        return ingredient.getStock() == 0;
    }
    
    public void subtract1FromPreppedIngredientsStock(PreppedIngredient ingredient) {
        ingredient.setStock(ingredient.getStock() - 1);
    }
    
    public void updatePreppedIngredientStockLevel(PreppedIngredient preppedIngredient, int newStockLevel) {
        preppedIngredient.setStock(newStockLevel);
        preppedIngredientRepo.save(preppedIngredient);
    }
    
    private Integer computeCapacityThreshold(PreppedIngredient ingredient) {
        return (int) Math.floor(0.1 * ingredient.getCapacity());
    }
    
    private PreppedIngredient convertPreppedIngredientToPreppedIngredient(PreppedIngredientRequest preppedIngredientRequest) throws RawIngredientNotFoundException {
        List<RawIngredient> rawIngredients = new ArrayList<>();
        
        // check to make sure all raw ingredients provided for prepped ingredient are valid
        for (String rawIngredientId : preppedIngredientRequest.getRawIngredientsNeeded()) {
            Optional<RawIngredient> rawIngredient = rawIngredientService.findRawIngredientById(rawIngredientId);
            
            if (!rawIngredient.isPresent())
                throw new RawIngredientNotFoundException(MessageFormat.format("{0} is not a valid Raw Ingredient ID",
                        rawIngredientId));
            else
                rawIngredients.add(rawIngredient.get());
        }
        
        return new PreppedIngredient(preppedIngredientRequest.getName(), preppedIngredientRequest.getType(),
                preppedIngredientRequest.getPrice(), rawIngredients, preppedIngredientRequest.getStock(),
                preppedIngredientRequest.getCapacity());
    }
    
    private String safeAddPreppedIngredient(PreppedIngredient ingredient) throws PreppedIngredientAlreadyExistsException, FailedToAddPreppedIngredientException {
        if (isPreppedIngredientAlreadyExists(ingredient.getName())) {
            log.info("did not prepped ingredient '{}' since another prepped ingredient with same name exists",
                    ingredient);
            throw new PreppedIngredientAlreadyExistsException();
        }
        
        try {
            preppedIngredientRepo.insert(ingredient);
            log.info("successfully added prepped ingredient: {}", ingredient);
            
            return preppedIngredientRepo.findByName(ingredient.getName()).get().getId();
        } catch (Exception e) {
            throw new FailedToAddPreppedIngredientException();
        }
    }
    
    private void addMultiplePreppedIngredient(List<PreppedIngredient> ingredients) throws PreppedIngredientAlreadyExistsException, FailedToAddPreppedIngredientException {
        for (PreppedIngredient ing : ingredients) {
            if (isPreppedIngredientAlreadyExists(ing.getName())) {
                throw new PreppedIngredientAlreadyExistsException();
            }
        }
        
        try {
            for (PreppedIngredient ing : ingredients)
                preppedIngredientRepo.insert(ing);
        } catch (Exception e) {
            throw new FailedToAddPreppedIngredientException();
        }
    }
    
    private boolean isPreppedIngredientAlreadyExists(String name) {
        return preppedIngredientRepo.findByName(name).isPresent();
    }
}

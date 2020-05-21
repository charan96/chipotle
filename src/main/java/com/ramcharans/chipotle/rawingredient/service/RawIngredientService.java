package com.ramcharans.chipotle.rawingredient.service;

import com.ramcharans.chipotle.rawingredient.controller.RawIngredientRequest;
import com.ramcharans.chipotle.rawingredient.dao.RawIngredientRepository;
import com.ramcharans.chipotle.rawingredient.exceptions.FailedToAddRawIngredientException;
import com.ramcharans.chipotle.rawingredient.exceptions.RawIngredientAlreadyExistsException;
import com.ramcharans.chipotle.rawingredient.exceptions.RawIngredientNotFoundException;
import com.ramcharans.chipotle.rawingredient.model.RawIngredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RawIngredientService {
    private static final Logger log = LoggerFactory.getLogger(RawIngredientService.class);
    
    private RawIngredientRepository rawIngredientRepo;
    
    public RawIngredientService(RawIngredientRepository rawIngredientRepository) {
        this.rawIngredientRepo = rawIngredientRepository;
    }
    
    public String addRawIngredientFromRawIngredientRequest(RawIngredientRequest rawIngredientRequest) throws
            RawIngredientAlreadyExistsException, FailedToAddRawIngredientException {
        RawIngredient ingredient = convertRawIngredientRequestToRawIngredient(rawIngredientRequest);
        return safeAddRawIngredient(ingredient);
    }
    
    public void addMultipleRawIngredientsFromIngredientRequestList(List<RawIngredientRequest> rawIngredientRequests) throws
            RawIngredientAlreadyExistsException, FailedToAddRawIngredientException {
        List<RawIngredient> rawIngredients = rawIngredientRequests
                .stream()
                .map(this::convertRawIngredientRequestToRawIngredient)
                .collect(Collectors.toList());
        
        addMultipleRawIngredients(rawIngredients);
    }
    
    public List<RawIngredient> getAllRawIngredients() {
        return rawIngredientRepo.findAll();
    }
    
    public Optional<RawIngredient> findRawIngredientById(String id) {
        return rawIngredientRepo.findById(id);
    }
    
    public Optional<RawIngredient> findRawIngredientByName(String name) {
        return rawIngredientRepo.findByName(name);
    }
    
    public void deleteRawIngredientById(String id) throws RawIngredientNotFoundException {
        Optional<RawIngredient> ing = rawIngredientRepo.findById(id);
        
        if (ing.isPresent())
            rawIngredientRepo.delete(ing.get());
        else
            throw new RawIngredientNotFoundException();
    }
    
    private RawIngredient convertRawIngredientRequestToRawIngredient(RawIngredientRequest rawIngredientRequest) {
        return new RawIngredient(rawIngredientRequest.getName(), rawIngredientRequest.getStock());
    }
    
    private void addMultipleRawIngredients(List<RawIngredient> ingredients) throws FailedToAddRawIngredientException,
            RawIngredientAlreadyExistsException {
        for (RawIngredient ing : ingredients) {
            if (isRawIngredientAlreadyExists(ing.getName())) {
                // FIXME: add err message
                throw new RawIngredientAlreadyExistsException();
            }
        }
        
        try {
            for (RawIngredient ing : ingredients)
                rawIngredientRepo.insert(ing);
        } catch (Exception e) {
            // fixme: add err message
            throw new FailedToAddRawIngredientException();
        }
    }
    
    private String safeAddRawIngredient(RawIngredient rawIngredient) throws RawIngredientAlreadyExistsException,
            FailedToAddRawIngredientException {
        if (isRawIngredientAlreadyExists(rawIngredient.getName())) {
            log.info("did not add raw ingredient '{}' since another raw ingredient with same name exists",
                    rawIngredient);
            throw new RawIngredientAlreadyExistsException();
        }
        
        try {
            rawIngredientRepo.insert(rawIngredient);
            log.info("successfully added raw ingredient: {}", rawIngredient);
            
            return rawIngredientRepo.findByName(rawIngredient.getName()).get().getId();
        } catch (Exception e) {
            throw new FailedToAddRawIngredientException();
        }
    }
    
    private boolean isRawIngredientAlreadyExists(String name) {
        return rawIngredientRepo.findByName(name).isPresent();
    }
}

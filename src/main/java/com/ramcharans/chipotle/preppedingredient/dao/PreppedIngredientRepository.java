package com.ramcharans.chipotle.preppedingredient.dao;

import com.ramcharans.chipotle.preppedingredient.model.PreppedIngredient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PreppedIngredientRepository extends MongoRepository<PreppedIngredient, String> {
    @Query("{'name' : ?0}")
    Optional<PreppedIngredient> findByName(String name);
    
    @Query("{'type' : ?0}")
    List<PreppedIngredient> findAllByType(PreppedIngredient.Type type);
}

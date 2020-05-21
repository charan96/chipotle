package com.ramcharans.chipotle.rawingredient.dao;

import com.ramcharans.chipotle.rawingredient.model.RawIngredient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface RawIngredientRepository extends MongoRepository<RawIngredient, String> {
    @Query("{'name' : ?0}")
    Optional<RawIngredient> findByName(String name);
}

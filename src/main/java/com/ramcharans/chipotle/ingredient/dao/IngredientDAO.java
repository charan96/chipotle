package com.ramcharans.chipotle.ingredient.dao;

import com.ramcharans.chipotle.ingredient.exceptions.IngredientAlreadyExistsException;
import com.ramcharans.chipotle.ingredient.exceptions.IngredientNotFoundException;
import com.ramcharans.chipotle.ingredient.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class IngredientDAO {

    @Autowired
    private MongoTemplate mongoTemplate;        // built automatically by Spring after reading application.properties

    public List<Ingredient> getAllIngredients() {
        return mongoTemplate.findAll(Ingredient.class);
    }

    public void safeAddIngredient(Ingredient ingredient) throws IngredientAlreadyExistsException {
        if (isIngredientAlreadyExists(ingredient))
            throw new IngredientAlreadyExistsException();
        else
            addIngredient(ingredient);
    }

    private boolean isIngredientAlreadyExists(Ingredient ingredient) {
        Optional<Ingredient> ing = findByName(ingredient.getName());
        return ing.isPresent();
    }

    public void addIngredient(Ingredient ingredient) {
        mongoTemplate.save(ingredient);
    }

    public void deleteIngredient(String id) throws IngredientNotFoundException {
        if (!findById(id).isPresent())
            throw new IngredientNotFoundException();

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        mongoTemplate.remove(query, Ingredient.class);
    }

    public Optional<Ingredient> findById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        Ingredient ing = mongoTemplate.findOne(query, Ingredient.class);
        return Optional.ofNullable(ing);
    }

    public List<Ingredient> findByType(Ingredient.Type type) {
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is(type));

        return mongoTemplate.find(query, Ingredient.class);
    }

    public Optional<Ingredient> findByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));

        Ingredient ing = mongoTemplate.findOne(query, Ingredient.class);
        return Optional.ofNullable(ing);
    }
}

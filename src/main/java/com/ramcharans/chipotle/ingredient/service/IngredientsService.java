package com.ramcharans.chipotle.ingredient.service;

import com.ramcharans.chipotle.ingredient.dao.IngredientDAO;
import com.ramcharans.chipotle.ingredient.exceptions.FailedToAddIngredientException;
import com.ramcharans.chipotle.ingredient.exceptions.IngredientAlreadyExistsException;
import com.ramcharans.chipotle.ingredient.exceptions.IngredientNotFoundException;
import com.ramcharans.chipotle.ingredient.model.Ingredient;
import com.ramcharans.chipotle.ingredient.model.Ingredient.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientsService {
    @Autowired
    IngredientDAO ingredientsDAO;

    public List<Ingredient> getAvailableIngredients() {
        return ingredientsDAO.getAllIngredients();
    }

    public String addIngredient(Ingredient ingredient) throws FailedToAddIngredientException,
            IngredientAlreadyExistsException {
        try {
            ingredientsDAO.safeAddIngredient(ingredient);
            return ingredientsDAO.findByName(ingredient.getName()).get().getId();
        } catch (IngredientAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new FailedToAddIngredientException(MessageFormat.format("failed to add ingredient: {0}", ingredient));
        }
    }

    public void addAllIngredients(List<Ingredient> ingredients) throws FailedToAddIngredientException,
            IngredientAlreadyExistsException {

        // note: essentially doing transaction here; if any of the ingredients exist, none of them are added
        for (Ingredient ing : ingredients) {
            if (ingredientsDAO.findById(ing.getId()).isPresent())
                throw new IngredientAlreadyExistsException(MessageFormat.format(
                        "Ingredient {0} already exists; none of the provided ingredients were added", ing));
        }

        try {
            for (Ingredient ing : ingredients)
                ingredientsDAO.addIngredient(ing);
        } catch (Exception e) {
            throw new FailedToAddIngredientException();
        }
    }

    public Optional<Ingredient> getIngredientById(String id) {
        return ingredientsDAO.findById(id);
    }

    public List<Ingredient> getIngredientsByType(Type type) {
        return ingredientsDAO.findByType(type);
    }

    public void deleteIngredient(String id) throws IngredientNotFoundException {
        ingredientsDAO.deleteIngredient(id);
    }
}

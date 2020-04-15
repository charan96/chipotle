package com.ramcharans.chipotle.ingredient.service;

import com.ramcharans.chipotle.ingredient.dao.IngredientsDAO;
import com.ramcharans.chipotle.ingredient.exceptions.FailedToAddIngredientException;
import com.ramcharans.chipotle.ingredient.exceptions.IngredientAlreadyExistsException;
import com.ramcharans.chipotle.ingredient.exceptions.ValueExistsInListException;
import com.ramcharans.chipotle.ingredient.model.Ingredient;
import com.ramcharans.chipotle.ingredient.model.Ingredient.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientsService {
    @Autowired
    IngredientsDAO ingredientsDAO;

    public List<Ingredient> getAvailableIngredients() {
        return ingredientsDAO.getAvailableIngredients();
    }

    public void addIngredient(Ingredient ingredient) throws FailedToAddIngredientException,
            IngredientAlreadyExistsException {
        try {
            ingredientsDAO.addIngredientToAvailableIngredients(ingredient);
        } catch (ValueExistsInListException e) {
            throw new IngredientAlreadyExistsException(MessageFormat.format(
                    "following ingredient already exists in DB: {}", ingredient));
        } catch (Exception e) {
            throw new FailedToAddIngredientException(MessageFormat.format("failed to add ingredient: {}", ingredient));
        }
    }

    public Optional<Ingredient> getIngredientById(Long id) {
        return getAvailableIngredients()
                .stream()
                .filter(ingredient1 -> ingredient1.getId().equals(id))
                .findAny();
    }

    public List<Ingredient> getIngredientsByType(Type type) {
        return getAvailableIngredients()
                .stream()
                .filter(ingredient -> ingredient.getType().equals(type))
                .collect(Collectors.toList());
    }
}

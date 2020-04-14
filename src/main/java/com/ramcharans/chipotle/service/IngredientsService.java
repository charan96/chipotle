package com.ramcharans.chipotle.service;

import com.ramcharans.chipotle.dao.IngredientsDAO;
import com.ramcharans.chipotle.model.Ingredient;
import com.ramcharans.chipotle.model.Ingredient.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void addIngredient(Ingredient ingredient) {
        ingredientsDAO.addIngredientToAvailableIngredients(ingredient);
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

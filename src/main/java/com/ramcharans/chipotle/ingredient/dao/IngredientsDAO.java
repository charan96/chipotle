package com.ramcharans.chipotle.ingredient.dao;

import com.ramcharans.chipotle.ingredient.exceptions.ValueExistsInListException;
import com.ramcharans.chipotle.ingredient.model.Ingredient;
import com.ramcharans.chipotle.ingredient.model.Ingredient.Type;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IngredientsDAO {
    private List<Ingredient> availableIngredients;

    public List<Ingredient> getAvailableIngredients() {
        if (availableIngredients == null)
            availableIngredients = loadAvailableIngredientsFromDB();

        return availableIngredients;
    }

    public void addIngredientToAvailableIngredients(Ingredient ingredient) throws ValueExistsInListException {
        List<Ingredient> ingredients = getAvailableIngredients();

        if (ingredients.stream().anyMatch(ing -> isSameIngredient(ing, ingredient)))
            throw new ValueExistsInListException();

        ingredient.setId(createNewIngredientId());
        updateIngredientsInDB(ingredient);

        availableIngredients.add(ingredient);
    }

    private boolean isSameIngredient(Ingredient i1, Ingredient i2) {
        boolean nameEquivalency = i1.getName().equals(i2.getName());
        boolean typeEquivalency = i1.getType().equals(i2.getType());

        return nameEquivalency && typeEquivalency;
    }

    private Long createNewIngredientId() {
        Long maxId = Collections.max(availableIngredients
                .stream()
                .map(Ingredient::getId)
                .collect(Collectors.toList()));

        return maxId + 1;
    }


    public List<Ingredient> loadAvailableIngredientsFromDB() {
        List<Ingredient> availableIngredients = new ArrayList<>();

        availableIngredients.add(new Ingredient(101L, "White Rice", Type.RICE, 0.00));
        availableIngredients.add(new Ingredient(102L, "Brown Rice", Type.RICE, 0.00));
        availableIngredients.add(new Ingredient(103L, "Black Beans", Type.BEANS, 0.00));
        availableIngredients.add(new Ingredient(104L, "Pinto Beans", Type.BEANS, 0.00));
        availableIngredients.add(new Ingredient(105L, "Chicken", Type.MEAT, 6.80));
        availableIngredients.add(new Ingredient(106L, "Steak", Type.MEAT, 7.80));
        availableIngredients.add(new Ingredient(107L, "Carnitas", Type.MEAT, 7.20));
        availableIngredients.add(new Ingredient(108L, "Queso", Type.SALSA, 0.75));
        availableIngredients.add(new Ingredient(109L, "Fajitas", Type.VEGGIE, 0.00));
        availableIngredients.add(new Ingredient(110L, "Lettuce", Type.VEGGIE, 0.00));
        availableIngredients.add(new Ingredient(111L, "Cheese", Type.ADDON, 0.50));
        availableIngredients.add(new Ingredient(112L, "Corn", Type.ADDON, 0.00));
        availableIngredients.add(new Ingredient(113L, "Guacamole", Type.ADDON, 1.00));
        availableIngredients.add(new Ingredient(114L, "Sour Cream", Type.ADDON, 0.00));
        availableIngredients.add(new Ingredient(115L, "Mild Salsa", Type.SALSA, 0.00));
        availableIngredients.add(new Ingredient(116L, "Medium Salsa", Type.SALSA, 0.00));
        availableIngredients.add(new Ingredient(117L, "Hot Salsa", Type.SALSA, 0.00));
        availableIngredients.add(new Ingredient(118L, "Jalapeno", Type.ADDON, 0.00));
        availableIngredients.add(new Ingredient(119L, "Onion", Type.ADDON, 0.00));
        availableIngredients.add(new Ingredient(120L, "Parmesan", Type.ADDON, 0.00));

        return availableIngredients;
    }

    public void updateIngredientsInDB(Ingredient ingredient) {
        System.out.println("updating DB by adding new Ingredient: " + ingredient.toString());
    }
}

package com.ramcharans.chipotle.mealingredient.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MealIngredientNotFoundException extends Exception {
    public MealIngredientNotFoundException(String message) {
        super(message);
    }
}

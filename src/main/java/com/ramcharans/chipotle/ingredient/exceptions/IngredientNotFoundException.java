package com.ramcharans.chipotle.ingredient.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IngredientNotFoundException extends Exception {
    public IngredientNotFoundException(String message) {
        super(message);
    }
}

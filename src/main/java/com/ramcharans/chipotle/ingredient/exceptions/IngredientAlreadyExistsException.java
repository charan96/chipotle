package com.ramcharans.chipotle.ingredient.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IngredientAlreadyExistsException extends Exception {
    public IngredientAlreadyExistsException(String message) {
        super(message);
    }
}

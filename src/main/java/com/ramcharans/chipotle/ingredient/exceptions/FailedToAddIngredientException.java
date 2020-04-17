package com.ramcharans.chipotle.ingredient.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FailedToAddIngredientException extends Exception {
    public FailedToAddIngredientException(String message) {
        super(message);
    }
}

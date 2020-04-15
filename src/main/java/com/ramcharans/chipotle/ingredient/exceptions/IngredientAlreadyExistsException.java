package com.ramcharans.chipotle.ingredient.exceptions;

public class IngredientAlreadyExistsException extends Exception {
    public IngredientAlreadyExistsException(String message) {
        super(message);
    }
}

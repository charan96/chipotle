package com.ramcharans.chipotle.preppedingredient.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PreppedIngredientAlreadyExistsException extends Exception {
    public PreppedIngredientAlreadyExistsException(String message) {
        super(message);
    }
}

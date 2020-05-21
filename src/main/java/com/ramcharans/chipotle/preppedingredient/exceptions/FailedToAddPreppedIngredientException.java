package com.ramcharans.chipotle.preppedingredient.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FailedToAddPreppedIngredientException extends Exception {
    public FailedToAddPreppedIngredientException(String message) {
        super(message);
    }
}

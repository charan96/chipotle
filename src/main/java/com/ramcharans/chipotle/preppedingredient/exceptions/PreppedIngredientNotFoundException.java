package com.ramcharans.chipotle.preppedingredient.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PreppedIngredientNotFoundException extends Exception {
    public PreppedIngredientNotFoundException(String message) {
        super(message);
    }
}

package com.ramcharans.chipotle.rawingredient.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RawIngredientNotFoundException extends Exception {
    public RawIngredientNotFoundException(String message) {
        super(message);
    }
}

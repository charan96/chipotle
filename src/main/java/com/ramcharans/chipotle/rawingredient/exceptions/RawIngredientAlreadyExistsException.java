package com.ramcharans.chipotle.rawingredient.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RawIngredientAlreadyExistsException extends Exception {
    public RawIngredientAlreadyExistsException(String message) {
        super(message);
    }
}

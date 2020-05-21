package com.ramcharans.chipotle.rawingredient.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FailedToAddRawIngredientException extends Exception {
    public FailedToAddRawIngredientException(String message) {
        super(message);
    }
}

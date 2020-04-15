package com.ramcharans.chipotle.ingredient.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValueExistsInListException extends Exception {
    public ValueExistsInListException(String message) {
        super(message);
    }
}

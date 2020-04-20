package com.ramcharans.chipotle.ingredient.controller;

import com.ramcharans.chipotle.ingredient.model.Ingredient;
import io.swagger.annotations.ApiModelProperty;

public class IngredientRequest {
    @ApiModelProperty(value = "Name of the Ingredient")
    String name;

    @ApiModelProperty(value = "Type of the Ingredient")
    Ingredient.Type type;

    @ApiModelProperty(value = "Price of the Ingredient")
    Double price;
}

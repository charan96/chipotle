package com.ramcharans.chipotle.ingredient.controller;

import com.ramcharans.chipotle.ingredient.model.Ingredient;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientRequest {
    @ApiModelProperty(value = "Name of the Ingredient")
    @NotNull
    String name;

    @ApiModelProperty(value = "Type of the Ingredient")
    @NotNull
    Ingredient.Type type;

    @ApiModelProperty(value = "Price of the Ingredient")
    @Min(value = 0, message = "cannot be less than 0")
    Double price;
}

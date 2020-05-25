package com.ramcharans.chipotle.rawingredient.controller;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawIngredientRequest {
    @ApiModelProperty(value = "Name of Raw Ingredient")
    @NotNull
    String name;
}

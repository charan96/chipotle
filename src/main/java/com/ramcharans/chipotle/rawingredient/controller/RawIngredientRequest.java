package com.ramcharans.chipotle.rawingredient.controller;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawIngredientRequest {
    @ApiModelProperty(value = "Name of Raw Ingredient")
    @NotNull
    String name;
    
    @ApiModelProperty(value = "Amount of Stock of the Raw Ingredient")
    @Min(value = 0, message = "Cannot be less than 0")
    int stock;
    
    @ApiModelProperty(value = "storage capacity of the raw ingredient")
    @Min(value = 0, message = "cannot be less than 0")
    int capacity;
    
    public RawIngredientRequest(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.stock = 0;
    }
}

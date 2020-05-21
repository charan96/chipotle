package com.ramcharans.chipotle.mealingredient.controller;

import com.ramcharans.chipotle.preppedingredient.model.PreppedIngredient;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MealIngredientResponse {
    @ApiModelProperty(value = "prepped ingredient ID")
    String id;
    
    @ApiModelProperty(value = "name of the prepped ingredient")
    String name;
    
    @ApiModelProperty(value = "type of prepped ingredient")
    PreppedIngredient.Type type;
    
    @ApiModelProperty(value = "price of the prepped ingredient")
    Double price;
    
    public MealIngredientResponse(PreppedIngredient preppedIngredient) {
        this.id = preppedIngredient.getId();
        this.name = preppedIngredient.getName();
        this.type = preppedIngredient.getType();
        this.price = preppedIngredient.getPrice();
    }
}

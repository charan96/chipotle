package com.ramcharans.chipotle.preppedingredient.controller;

import com.ramcharans.chipotle.preppedingredient.model.PreppedIngredient;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreppedIngredientRequest {
    @ApiModelProperty(value = "name of the Prepped Ingredient")
    @NotNull
    String name;
    
    @ApiModelProperty(value = "Type of the Prepped Ingredient")
    @NotNull
    PreppedIngredient.Type type;
    
    @ApiModelProperty(value = "Price of the Ingredient")
    @NotNull
    @Min(value = 0, message = "cannot be less than 0")
    Double price;
    
    @ApiModelProperty(value = "List of Raw Ingredient IDs need to prepare the Ingredient")
    @NotNull
    List<String> rawIngredientsNeeded;
    
    @ApiModelProperty(value = "stock of the Prepped Ingredient")
    @Min(value = 0, message = "cannot be less than 0")
    Integer stock;
    
    @ApiModelProperty(value = "storage capacity of the prepped ingredient")
    @Min(value = 0, message = "cannot be less than 0")
    Integer capacity;
    
    public PreppedIngredientRequest(String name, PreppedIngredient.Type type, Double price,
                                    List<String> rawIngredientsNeeded, Integer capacity) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.rawIngredientsNeeded = rawIngredientsNeeded;
        this.capacity = capacity;
        this.stock = 0;
    }
}

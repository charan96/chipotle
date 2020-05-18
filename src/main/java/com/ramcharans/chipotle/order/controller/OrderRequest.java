package com.ramcharans.chipotle.order.controller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class OrderRequest {
    @ApiModelProperty(value = "customer id")
    @NotNull
    private String customerId;

    @ApiModelProperty(value = "list of Ingredient IDs")
    @Size()
    private List<String> ingredientIds;
}

package com.ramcharans.chipotle.order.controller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    @ApiModelProperty(value = "customer id")
    private String customerId;

    @ApiModelProperty(value = "list of Ingredient IDs")
    private List<String> ingredientIds;
}

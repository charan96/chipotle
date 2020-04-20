package com.ramcharans.chipotle.order.controller;

import com.ramcharans.chipotle.ingredient.model.Ingredient;
import com.ramcharans.chipotle.order.model.Order;
import com.ramcharans.chipotle.payment.model.Payment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.*;

@Data
public class OrderResponse {
    @ApiModelProperty(value = "order ID")
    String id;

    @ApiModelProperty(value = "customer ID")
    String customerId;

    @ApiModelProperty(value = "list of Ingredient details (Name and Price only)")
    List<Map<String, String>> ingredients;

    @ApiModelProperty(value = "order total amount")
    Double total;

    @ApiModelProperty(value = "the payment object details")
    Payment payment;

    public OrderResponse(Order order) {
        id = order.getId();
        customerId = order.getCustomerId();
        total = order.getTotal();
        payment = order.getPayment();

        ingredients = new ArrayList<>();

        for (Ingredient ing : order.getIngredients())
            ingredients.add(new TreeMap<String, String>() {{
                put("name", ing.getName());
                put("price", ing.getPrice().toString());
            }});
    }
}

package com.ramcharans.chipotle.order.controller;

import com.ramcharans.chipotle.ingredient.model.Ingredient;
import com.ramcharans.chipotle.order.model.Order;
import com.ramcharans.chipotle.payment.model.Payment;
import lombok.Data;

import java.util.*;

@Data
public class OrderResponse {
    String id;
    String customerId;
    List<Map<String, String>> ingredients;
    Double total;
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

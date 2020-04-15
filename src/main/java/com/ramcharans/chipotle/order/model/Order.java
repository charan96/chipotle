package com.ramcharans.chipotle.order.model;


import com.ramcharans.chipotle.ingredient.model.Ingredient;
import com.ramcharans.chipotle.payment.model.Payment;
import lombok.Data;

import java.util.List;

@Data
public class Order {
    private Long id;
    private String customerName;
    private List<Ingredient> ingredients;
    private Double total;

    private boolean isPaid;
    private Payment payment;
}

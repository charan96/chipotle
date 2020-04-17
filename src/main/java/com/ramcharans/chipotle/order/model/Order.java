package com.ramcharans.chipotle.order.model;


import com.ramcharans.chipotle.ingredient.model.Ingredient;
import com.ramcharans.chipotle.payment.model.Payment;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
public class Order {
    @Id
    private Long id;
    private String customerName;

    private List<Ingredient> ingredients;
    private Double total;

    private boolean isPaid;
    private Payment payment;
}

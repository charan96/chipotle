package com.ramcharans.chipotle.order.model;


import com.ramcharans.chipotle.ingredient.model.Ingredient;
import com.ramcharans.chipotle.payment.model.Payment;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "order")
public class Order {
    @Id
    private String id;
    private String customerId;

    @DBRef
    private List<Ingredient> ingredients;
    private Double total;

    @DBRef
    private Payment payment;
    private boolean isFulfilled;
}

package com.ramcharans.chipotle.model;


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

package com.ramcharans.chipotle.order.controller;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private String customerId;
    private List<String> ingredientIds;
}

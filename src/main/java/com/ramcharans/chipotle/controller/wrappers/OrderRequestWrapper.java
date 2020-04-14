package com.ramcharans.chipotle.controller.wrappers;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestWrapper {
    private String customerName;
    private List<Long> ingredientIds;
}

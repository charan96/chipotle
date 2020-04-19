package com.ramcharans.chipotle.order.controller;

import com.ramcharans.chipotle.ingredient.exceptions.IngredientNotFoundException;
import com.ramcharans.chipotle.order.exceptions.OrderNotFoundException;
import com.ramcharans.chipotle.order.model.Order;
import com.ramcharans.chipotle.order.service.OrderService;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping(path = "/", produces = "application/json")
    public ResponseEntity<Object> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @PostMapping(path = "/submit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> submitOrder(@RequestBody OrderRequest orderRequest) {
        try {
            Order order = orderService.buildAndSaveOrder(orderRequest.getCustomerId(), orderRequest.getIngredientIds());
            return new ResponseEntity<>(new OrderResponse(order), HttpStatus.OK);
        } catch (IngredientNotFoundException e) {
            return new ResponseEntity<>("one of the ingredient IDs provided does not exist", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/find/id", produces = "application/json")
    public ResponseEntity<Object> findOrderById(@RequestParam String id) {
        try {
            Order order = orderService.findOrder(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>("no order found with given ID", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/find/fulfilled", produces = "application/json")
    public ResponseEntity<Object> findOrderByIsFulfilled(@RequestParam boolean fulfilled) {
        List<Order> orders = orderService.findOrdersByIsFulfilled(fulfilled);

        if (orders.equals(Collections.emptyList()))
            return new ResponseEntity<>(MessageFormat.format("No orders found with fulfilled={0}", fulfilled),
                    HttpStatus.OK);
        else
            return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}

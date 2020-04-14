package com.ramcharans.chipotle.controller;

import com.ramcharans.chipotle.controller.wrappers.OrderRequestWrapper;
import com.ramcharans.chipotle.model.Order;
import com.ramcharans.chipotle.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping(path = "/", produces = "application/json")
    public ResponseEntity<List<Order>> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @PostMapping(path = "/submit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Order> submitOrder(@RequestBody OrderRequestWrapper orderRequest) {
        Order order = orderService.buildOrder(orderRequest.getCustomerName(), orderRequest.getIngredientIds());
        orderService.saveOrder(order);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping(path = "/find/{order_id}", produces = "application/json")
    public ResponseEntity<Order> findOrderById(@PathVariable Long order_id) {
        Optional<Order> order = orderService.findOrder(order_id);

        if (order.isPresent())
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

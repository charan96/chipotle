package com.ramcharans.chipotle.order.controller;

import com.ramcharans.chipotle.mealingredient.exceptions.MealIngredientNotFoundException;
import com.ramcharans.chipotle.order.exceptions.OrderNotFoundException;
import com.ramcharans.chipotle.order.model.Order;
import com.ramcharans.chipotle.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

@RestController
@Api(value = "Order Management System")
@RequestMapping(path = "/order")
public class OrderController {
    OrderService orderService;
    
    public static final Logger log = LoggerFactory.getLogger(OrderController.class);
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @ApiOperation(value = "get all orders", response = Order.class, responseContainer = "List")
    @GetMapping(path = "", produces = "application/json")
    public ResponseEntity<Object> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }
    
    @ApiOperation(value = "submit the Order", response = OrderResponse.class)
    @PostMapping(path = "/submit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> submitOrder(@RequestBody OrderRequest orderRequest) {
        try {
            Order order = orderService.buildAndSaveOrder(orderRequest.getCustomerId(), orderRequest.getIngredientIds());
            return new ResponseEntity<>(new OrderResponse(order), HttpStatus.OK);
        } catch (MealIngredientNotFoundException e) {
            return new ResponseEntity<>("one of the ingredient IDs provided does not exist", HttpStatus.BAD_REQUEST);
        }
    }
    
    @ApiOperation(value = "find order by ID", response = Order.class)
    @GetMapping(path = "/find/id", produces = "application/json")
    public ResponseEntity<Object> findOrderById(@RequestParam String id) {
        try {
            Order order = orderService.findOrder(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>("no order found with given ID", HttpStatus.NOT_FOUND);
        }
    }
    
    @ApiOperation(value = "get a list of all fulfilled/unfulfilled (based on argument) orders", response =
            Order.class, responseContainer = "List")
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

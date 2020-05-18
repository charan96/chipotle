package com.ramcharans.chipotle.chef.service;

import com.ramcharans.chipotle.events.mealfulfilled.MealFulfilledEventProducer;
import com.ramcharans.chipotle.order.exceptions.OrderNotFoundException;
import com.ramcharans.chipotle.order.model.Order;
import com.ramcharans.chipotle.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

import static java.lang.Thread.sleep;

@Service
public class ChefService {
    @Autowired
    MealFulfilledEventProducer mealFulfilledEventProducer;

    @Autowired
    OrderService orderService;

    public void prepareMealFromOrder(String orderId) {
        try {
            Order order = orderService.findOrder(orderId);

            sleep(10000);
            sendMealFulfilledEvent(order.getId());
        } catch (OrderNotFoundException e) {
            throw new RuntimeException(MessageFormat.format("No order found with ID: {0}", orderId));
        } catch (InterruptedException e) {
            throw new RuntimeException("failed to prepare meal");
        }
    }

    public void sendMealFulfilledEvent(String orderId) {
        mealFulfilledEventProducer.send(orderId);
    }
}

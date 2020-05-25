package com.ramcharans.chipotle.chef.service;

import com.ramcharans.chipotle.blockedorderqueue.exceptions.BlockedOrderQueueEmptyException;
import com.ramcharans.chipotle.blockedorderqueue.service.BlockedOrderQueueService;
import com.ramcharans.chipotle.events.mealfulfilled.MealFulfilledEventProducer;
import com.ramcharans.chipotle.mealingredient.service.MealIngredientService;
import com.ramcharans.chipotle.order.exceptions.OrderNotFoundException;
import com.ramcharans.chipotle.order.model.Order;
import com.ramcharans.chipotle.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

import static java.lang.Thread.sleep;

@Service
public class ChefService {
    private final OrderService orderService;
    private final MealIngredientService mealIngredientService;
    private final BlockedOrderQueueService blockedOrderQueueService;
    
    private static final Logger log = LoggerFactory.getLogger(ChefService.class);
    
    private final MealFulfilledEventProducer mealFulfilledEventProducer;
    
    public ChefService(OrderService orderService, BlockedOrderQueueService queueService,
                       MealIngredientService mealIngredientService,
                       MealFulfilledEventProducer mealFulfilledEventProducer) {
        this.orderService = orderService;
        this.blockedOrderQueueService = queueService;
        this.mealIngredientService = mealIngredientService;
        
        this.mealFulfilledEventProducer = mealFulfilledEventProducer;
    }
    
    private void prepareMealFromOrder(Order order) {
        try {
            mealIngredientService.subtractFromPreppedIngredientsStock(order.getIngredients());
            sleep(10000);
            sendMealFulfilledEvent(order.getId());
        } catch (InterruptedException e) {
            // note: may need to change this into an event or find a better solution than runtime exception
            throw new RuntimeException("failed to prepare meal");
        }
    }
    
    /*
     * check if all ingredients stock is greater than 0
     * if true -> prepare the meal
     * if not -> add order to blockedOrderQueue
     */
    public void prepareMeal(String orderId) {
        try {
            Order order = orderService.findOrder(orderId);
            
            if (mealIngredientService.isAllMealIngredientsAvailable(order.getIngredients())) {
                prepareMealFromOrder(order);
            } else {
                blockedOrderQueueService.addOrderToQueue(order);
            }
        } catch (OrderNotFoundException e) {
            throw new RuntimeException(MessageFormat.format("No order found with ID: {0}", orderId));
        }
    }
    
    public void prepareMealFromBlockedOrderQueue() {
        try {
            prepareMeal(blockedOrderQueueService.getNextOrderFromQueue().getId());
        } catch (BlockedOrderQueueEmptyException e) {
            log.warn("blocked order queue is empty");
        }
    }
    
    private void sendMealFulfilledEvent(String orderId) {
        mealFulfilledEventProducer.send(orderId);
    }
}

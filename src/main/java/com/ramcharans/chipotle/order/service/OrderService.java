package com.ramcharans.chipotle.order.service;

import com.ramcharans.chipotle.mealingredient.exceptions.MealIngredientNotFoundException;
import com.ramcharans.chipotle.mealingredient.service.MealIngredientService;
import com.ramcharans.chipotle.order.dao.OrderDAO;
import com.ramcharans.chipotle.order.exceptions.OrderNotFoundException;
import com.ramcharans.chipotle.order.model.Order;
import com.ramcharans.chipotle.preppedingredient.model.PreppedIngredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    MealIngredientService mealIngredientService;
    OrderDAO orderDAO;
    
    public static final Logger log = LoggerFactory.getLogger(OrderService.class);
    
    public OrderService(MealIngredientService mealIngredientService, OrderDAO orderDAO) {
        this.mealIngredientService = mealIngredientService;
        this.orderDAO = orderDAO;
    }
    
    public Order buildAndSaveOrder(String customerId, List<String> ingredientIds) throws MealIngredientNotFoundException {
        Order order = buildOrder(customerId, ingredientIds);
        saveOrder(order);
        
        return order;
    }
    
    public Order buildOrder(String customerId, List<String> ingredientIds) throws MealIngredientNotFoundException {
        // NOTE: the order building logic should go here instead of a separate Factory class since building the order
        // NOTE contd.: is part of the business logic that needs to be handled by the service; in case, the Order class
        // NOTE contd.: changes in the future, we only need to look here to make changes; the factory class will lead to
        // NOTE contd.: scattering of the business logic.
        
        validateIngredientIds(ingredientIds);
        
        Order order = new Order();
        
        order.setCustomerId(customerId);
        order.setIngredients(getIngredientsListFromIngredientIds(ingredientIds));
        order.setTotal(calculateOrderTotal(order));
        
        return order;
    }
    
    public void saveOrder(Order order) {
        // TODO: replace orderdao with MongoRepository and look at the proxy class generated
        orderDAO.saveOrder(order);
    }
    
    private void validateIngredientIds(List<String> ingredientIds) throws MealIngredientNotFoundException {
        for (String ingredientId : ingredientIds) {
            if (!mealIngredientService.findIngredientById(ingredientId).isPresent()) {
                log.info("no ingredient found for id: {}", ingredientId);
                throw new MealIngredientNotFoundException(MessageFormat.format(
                        "No ingredient found for id: {0}", ingredientId));
            }
        }
    }
    
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }
    
    public Order findOrder(String id) throws OrderNotFoundException {
        Optional<Order> order = orderDAO.findById(id);
        
        if (order.isPresent())
            return order.get();
        else
            throw new OrderNotFoundException();
    }
    
    public List<Order> findOrdersByIsFulfilled(boolean isFulfilled) {
        return orderDAO.findByIsFulfilled(isFulfilled);
    }
    
    public List<PreppedIngredient> getIngredientsListFromIngredientIds(List<String> ingredientIds) throws MealIngredientNotFoundException {
        // NOTE: this method currently will only be called after ingredient ID validation has
        //  finished in build order; this method throws the exception because if we use it somewhere else,
        //  then they'd need to know that the Ingredient may not exist
        
        List<PreppedIngredient> ingredients = new ArrayList<>();
        
        for (String ingredientId : ingredientIds) {
            Optional<PreppedIngredient> ing = mealIngredientService.findIngredientById(ingredientId);
            
            if (ing.isPresent())
                ingredients.add(ing.get());
            else
                throw new MealIngredientNotFoundException(MessageFormat.format(
                        "Ingredient with ID: {0} not found", ingredientId));
        }
        
        return ingredients;
    }
    
    private Double calculateOrderTotal(Order order) throws MealIngredientNotFoundException {
        Double minMeatPrice = Collections.min(mealIngredientService.findIngredientsByType(PreppedIngredient.Type.MEAT)
                .stream()
                .map(PreppedIngredient::getPrice)
                .collect(Collectors.toList()));
        
        Double maxOrderMeatPrice = order.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getType().equals(PreppedIngredient.Type.MEAT))
                .max(Comparator.comparing(PreppedIngredient::getPrice))
                .map(PreppedIngredient::getPrice)
                .orElse(minMeatPrice);
        
        Double sumOrderExtraPrice = order.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getType().equals(PreppedIngredient.Type.ADDON) ||
                        ingredient.getType().equals(PreppedIngredient.Type.SALSA))
                .map(PreppedIngredient::getPrice)
                .reduce(0.0, Double::sum);
        
        return maxOrderMeatPrice + sumOrderExtraPrice;
    }
    
    public void markOrderAsFulfilled(String orderId) {
        try {
            Order order = findOrder(orderId);
            order.setFulfilled(true);
            saveOrder(order);
        } catch (OrderNotFoundException e) {
            throw new RuntimeException(MessageFormat.format("Order not found with ID: {0}", orderId));
        }
    }
}

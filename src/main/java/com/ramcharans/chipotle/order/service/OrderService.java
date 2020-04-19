package com.ramcharans.chipotle.order.service;

import com.ramcharans.chipotle.ingredient.exceptions.IngredientNotFoundException;
import com.ramcharans.chipotle.order.dao.OrderDAO;
import com.ramcharans.chipotle.ingredient.service.IngredientsService;
import com.ramcharans.chipotle.ingredient.model.Ingredient;
import com.ramcharans.chipotle.order.exceptions.OrderNotFoundException;
import com.ramcharans.chipotle.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    IngredientsService ingredientsService;

    @Autowired
    OrderDAO orderDAO;

    public Order buildAndSaveOrder(String customerId, List<String> ingredientIds) throws IngredientNotFoundException {
        Order order = buildOrder(customerId, ingredientIds);
        saveOrder(order);

        return order;
    }

    public Order buildOrder(String customerId, List<String> ingredientIds) throws IngredientNotFoundException {
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
        orderDAO.saveOrder(order);
    }

    private void validateIngredientIds(List<String> ingredientIds) throws IngredientNotFoundException {
        for (String ingredientId : ingredientIds) {
            if (!ingredientsService.getIngredientById(ingredientId).isPresent())
                throw new IngredientNotFoundException(MessageFormat.format(
                        "No ingredient found for id: {0}", ingredientId));
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

    public List<Ingredient> getIngredientsListFromIngredientIds(List<String> ingredientIds) throws IngredientNotFoundException {
        // NOTE: this method currently will only be called after ingredient ID validation has finished in build order;
        // NOTE contd.: this method throws the exception because if we use it somewhere else, then they'd need to know
        // NOTE contd.: that the Ingredient may not exist

        List<Ingredient> ingredients = new ArrayList<>();

        for (String ingredientId : ingredientIds) {
            Optional<Ingredient> ing = ingredientsService.getIngredientById(ingredientId);

            if (ing.isPresent())
                ingredients.add(ing.get());
            else
                throw new IngredientNotFoundException(MessageFormat.format(
                        "Ingredient with ID: {0} not found", ingredientId));
        }

        return ingredients;
    }

    private Double calculateOrderTotal(Order order) throws IngredientNotFoundException {
        Double minMeatPrice = Collections.min(ingredientsService.getIngredientsByType(Ingredient.Type.MEAT)
                .stream()
                .map(Ingredient::getPrice)
                .collect(Collectors.toList()));

        Double maxOrderMeatPrice = order.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getType().equals(Ingredient.Type.MEAT))
                .max(Comparator.comparing(Ingredient::getPrice))
                .map(Ingredient::getPrice)
                .orElse(minMeatPrice);

        Double sumOrderExtraPrice = order.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getType().equals(Ingredient.Type.ADDON) ||
                        ingredient.getType().equals(Ingredient.Type.SALSA))
                .map(Ingredient::getPrice)
                .reduce(0.0, Double::sum);

        return maxOrderMeatPrice + sumOrderExtraPrice;
    }
}

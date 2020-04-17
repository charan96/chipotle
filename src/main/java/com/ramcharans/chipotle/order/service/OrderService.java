package com.ramcharans.chipotle.order.service;

import com.ramcharans.chipotle.ingredient.exceptions.IngredientNotFoundException;
import com.ramcharans.chipotle.order.dao.OrderDAO;
import com.ramcharans.chipotle.ingredient.service.IngredientsService;
import com.ramcharans.chipotle.ingredient.model.Ingredient;
import com.ramcharans.chipotle.order.exceptions.OrderNotFoundException;
import com.ramcharans.chipotle.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    IngredientsService ingredientsService;

    @Autowired
    OrderDAO orderDAO;

    private Long createNewOrderId() {
        return Math.abs(new Random().nextLong());
    }

    public Order buildAndSaveOrder(String customerName, List<Long> ingredientIds) throws IngredientNotFoundException {
        Order order = buildOrder(customerName, ingredientIds);
        saveOrder(order);

        return order;
    }

    public Order buildOrder(String customerName, List<Long> ingredientIds) throws IngredientNotFoundException {
        // NOTE: the order building logic should go here instead of a separate Factory class since building the order
        // NOTE contd.: is part of the business logic that needs to be handled by the service; in case, the Order class
        // NOTE contd.: changes in the future, we only need to look here to make changes; the factory class will lead to
        // NOTE contd.: scattering of the business logic.
        Order order = new Order();

        order.setId(createNewOrderId());
        order.setCustomerName(customerName);

        List<Ingredient> orderIngredients = createIngredientListFromIds(ingredientIds);
        order.setIngredients(orderIngredients);
        order.setTotal(calculateOrderTotal(order));

        return order;
    }

    private List<Ingredient> createIngredientListFromIds(List<Long> ids) throws IngredientNotFoundException {
        List<Ingredient> ingredients = new ArrayList<>();

        for (Long id : ids) {
            if (ingredientsService.getIngredientById(id.toString()).isPresent())
                ingredients.add(ingredientsService.getIngredientById(id.toString()).get());
            else
                throw new IngredientNotFoundException("ingredient ID doesn't exist");
        }

        return ingredients;
    }

    public void saveOrder(Order order) {
        orderDAO.addOrder(order);
    }

    public List<Order> getAllOrders() {
        return orderDAO.getOrders();
    }

    public Order findOrder(Long id) throws OrderNotFoundException {
        Optional<Order> order = orderDAO.findOrderById(id);

        if (order.isPresent())
            return order.get();
        else
            throw new OrderNotFoundException();
    }

    private Double calculateOrderTotal(Order order) {
        Double minMeatPrice = Collections.min(ingredientsService.getAvailableIngredients()
                .stream()
                .filter(ingredient -> ingredient.getType().equals(Ingredient.Type.MEAT))
                .map(Ingredient::getPrice)
                .collect(Collectors.toList()));

        Double maxOrderMeatPrice = order.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getType().equals(Ingredient.Type.MEAT))
                .max(Comparator.comparing(Ingredient::getPrice))
                .map(Ingredient::getPrice)
                .orElse(minMeatPrice);

        Double sumOrderAddOnPrice = order.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getType().equals(Ingredient.Type.ADDON))
                .map(Ingredient::getPrice)
                .reduce(0.0, Double::sum);

        return maxOrderMeatPrice + sumOrderAddOnPrice;
    }
}

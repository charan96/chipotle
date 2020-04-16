package com.ramcharans.chipotle.order.dao;

import com.ramcharans.chipotle.order.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDAO {
    private List<Order> orders = new ArrayList<>();

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public Optional<Order> findOrderById(Long id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findAny();
    }

}

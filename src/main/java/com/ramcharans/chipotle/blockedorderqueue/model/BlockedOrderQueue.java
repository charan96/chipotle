package com.ramcharans.chipotle.blockedorderqueue.model;

import com.ramcharans.chipotle.order.model.Order;
import com.ramcharans.chipotle.preppedingredient.model.PreppedIngredient;
import lombok.Data;

import java.util.LinkedList;
import java.util.Queue;

@Data
public class BlockedOrderQueue {
    Queue<Order> queue;
    
    public BlockedOrderQueue() {
        queue = new LinkedList<>();
    }
}

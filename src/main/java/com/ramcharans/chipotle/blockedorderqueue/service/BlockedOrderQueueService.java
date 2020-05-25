package com.ramcharans.chipotle.blockedorderqueue.service;

import com.ramcharans.chipotle.blockedorderqueue.exceptions.BlockedOrderQueueEmptyException;
import com.ramcharans.chipotle.blockedorderqueue.model.BlockedOrderQueue;
import com.ramcharans.chipotle.order.model.Order;
import org.springframework.stereotype.Service;

@Service
public class BlockedOrderQueueService {
    private BlockedOrderQueue queue;
    
    public BlockedOrderQueueService() {
        this.queue = new BlockedOrderQueue();
    }
    
    public boolean isQueueEmpty() {
        return queue.getQueue().isEmpty();
    }
    
    public Order getNextOrderFromQueue() throws BlockedOrderQueueEmptyException {
        if (isQueueEmpty())
            throw new BlockedOrderQueueEmptyException();
        
        return queue.getQueue().remove();
    }
    
    public void addOrderToQueue(Order order) {
        queue.getQueue().add(order);
    }
}

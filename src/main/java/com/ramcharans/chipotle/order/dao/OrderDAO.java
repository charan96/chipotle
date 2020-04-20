package com.ramcharans.chipotle.order.dao;

import com.ramcharans.chipotle.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderDAO {
    @Autowired
    MongoTemplate mongoTemplate;

    public static final Logger log = LoggerFactory.getLogger(OrderDAO.class);

    public List<Order> getAllOrders() {
        return mongoTemplate.findAll(Order.class);
    }

    public String saveOrder(Order order) {
        // NOTE: mongoTemplate updates the ID instance variable the newly created ID in the order object
        //  when save is called
        mongoTemplate.save(order);
        return order.getId();
    }

    public Optional<Order> findById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        return Optional.ofNullable(mongoTemplate.findOne(query, Order.class));
    }

    public List<Order> findByIsFulfilled(boolean value) {
        Query query = new Query();
        query.addCriteria(Criteria.where("isFulfilled").is(value));

        return mongoTemplate.find(query, Order.class);
    }

    public List<Order> findByCustomerId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("customerId").is(id));

        return mongoTemplate.find(query, Order.class);
    }
}

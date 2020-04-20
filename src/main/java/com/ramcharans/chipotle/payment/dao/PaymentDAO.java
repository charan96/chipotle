package com.ramcharans.chipotle.payment.dao;

import com.ramcharans.chipotle.payment.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PaymentDAO {
    @Autowired
    MongoTemplate mongoTemplate;

    public void savePaymentToDB(Payment payment) {
        mongoTemplate.save(payment);
    }

    public Optional<Payment> getPaymentById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        return Optional.ofNullable(mongoTemplate.findOne(query, Payment.class));
    }
}

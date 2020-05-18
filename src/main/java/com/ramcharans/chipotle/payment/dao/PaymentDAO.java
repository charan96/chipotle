package com.ramcharans.chipotle.payment.dao;

import com.ramcharans.chipotle.payment.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PaymentDAO {
    MongoTemplate mongoTemplate;

    public PaymentDAO(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public static final Logger log = LoggerFactory.getLogger(PaymentDAO.class);

    public void savePaymentToDB(Payment payment) {
        mongoTemplate.save(payment);
    }

    public Optional<Payment> getPaymentById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        return Optional.ofNullable(mongoTemplate.findOne(query, Payment.class));
    }
}

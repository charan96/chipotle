package com.ramcharans.chipotle.dao;

import com.ramcharans.chipotle.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PaymentDAO {
    private List<Payment> payments = new ArrayList<>();

    public void savePaymentToDB(Payment payment) {
        payments.add(payment);
        System.out.println("payment saved to DB");
    }

    public Optional<Payment> getPaymentById(String id) {
        return payments.stream()
                .filter(payment -> payment.getId().equals(id))
                .findAny();
    }
}

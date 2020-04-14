package com.ramcharans.chipotle.service.transaction;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Qualifier("cashPayment")
public class CashPaymentTransactionService implements PaymentTransactionService {
    private final List<String> requiredPaymentTransactionDetails = Collections.emptyList();

    @Override
    public String createId() {
        return "CS" + Math.abs(new Random().nextLong());
    }

    @Override
    public void processTransaction(Map<String, String> paymentDetails) {
        // add transaction processing business logic here
    }

    @Override
    public boolean isValidPaymentDetails(Map<String, String> paymentDetails) {
        return paymentDetails.keySet().containsAll(requiredPaymentTransactionDetails);
    }

    @Override
    public List<String> getRequiredPaymentDetails() {
        return requiredPaymentTransactionDetails;
    }
}

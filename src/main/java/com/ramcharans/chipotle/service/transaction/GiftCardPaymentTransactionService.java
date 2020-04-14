package com.ramcharans.chipotle.service.transaction;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Qualifier("giftCardPayment")
public class GiftCardPaymentTransactionService implements PaymentTransactionService {
    private final List<String> requiredPaymentTransactionDetails = Arrays.asList("gc-num", "gc-bal");

    @Override
    public String createId() {
        return "GC" + Math.abs(new Random().nextLong());
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

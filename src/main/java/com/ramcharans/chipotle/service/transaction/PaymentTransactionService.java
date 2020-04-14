package com.ramcharans.chipotle.service.transaction;

import java.util.List;
import java.util.Map;

public interface PaymentTransactionService {
    public String createId();

    public void processTransaction(Map<String, String> paymentDetails);

    public boolean isValidPaymentDetails(Map<String, String> paymentDetails);

    public List<String> getRequiredPaymentDetails();
}

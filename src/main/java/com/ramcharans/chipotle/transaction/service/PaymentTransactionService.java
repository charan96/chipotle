package com.ramcharans.chipotle.transaction.service;

import com.ramcharans.chipotle.transaction.exceptions.InvalidPaymentDetailsException;

import java.util.List;
import java.util.Map;

public interface PaymentTransactionService {
    public String createId();

    public void processTransaction(Map<String, String> paymentDetails) throws InvalidPaymentDetailsException;
}

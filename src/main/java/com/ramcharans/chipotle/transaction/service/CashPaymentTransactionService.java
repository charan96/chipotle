package com.ramcharans.chipotle.transaction.service;

import com.ramcharans.chipotle.transaction.exceptions.InvalidPaymentDetailsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

@Service
@Qualifier("cashPayment")
public class CashPaymentTransactionService implements PaymentTransactionService {
    private final List<String> requiredPaymentTransactionDetails = Collections.emptyList();

    public static final Logger log = LoggerFactory.getLogger(CashPaymentTransactionService.class);

    @Override
    public String createId() {
        return "CS" + Math.abs(new Random().nextLong());
    }

    @Override
    public void processTransaction(Map<String, String> paymentDetails) throws InvalidPaymentDetailsException {
        // add transaction processing business logic here

        // for cash, there's no validation logic here
    }
}

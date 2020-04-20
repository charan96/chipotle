package com.ramcharans.chipotle.transaction.service;

import com.ramcharans.chipotle.transaction.exceptions.InvalidPaymentDetailsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Qualifier("giftCardPayment")
public class GiftCardPaymentTransactionService implements PaymentTransactionService {
    private final List<String> requiredPaymentTransactionDetails = Arrays.asList("gc-num", "gc-bal");

    public static final Logger log = LoggerFactory.getLogger(GiftCardPaymentTransactionService.class);

    @Override
    public String createId() {
        return "GC" + Math.abs(new Random().nextLong());
    }

    @Override
    public void processTransaction(Map<String, String> paymentDetails) throws InvalidPaymentDetailsException {
        // add transaction processing business logic here
        validatePaymentDetails(paymentDetails);
    }

    private void validatePaymentDetails(Map<String, String> paymentDetails) throws InvalidPaymentDetailsException {
        if (!paymentDetails.keySet().containsAll(requiredPaymentTransactionDetails))
            throw new InvalidPaymentDetailsException(MessageFormat.format(
                    "Must contain the following payment details: ", requiredPaymentTransactionDetails));
    }
}

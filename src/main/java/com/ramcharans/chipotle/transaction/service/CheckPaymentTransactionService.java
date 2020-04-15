package com.ramcharans.chipotle.transaction.service;

import com.ramcharans.chipotle.transaction.exceptions.InvalidPaymentDetailsException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Qualifier("checkPayment")
public class CheckPaymentTransactionService implements PaymentTransactionService {
    private final List<String> requiredPaymentTransactionDetails = Arrays.asList("bank", "acct-num", "chk-num", "amt");

    @Override
    public String createId() {
        return "CK" + Math.abs(new Random().nextLong());
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

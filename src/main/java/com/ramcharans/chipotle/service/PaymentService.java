package com.ramcharans.chipotle.service;

import com.ramcharans.chipotle.dao.PaymentDAO;
import com.ramcharans.chipotle.exceptions.InsufficientPaymentDetailsException;
import com.ramcharans.chipotle.exceptions.PaymentFailedException;
import com.ramcharans.chipotle.exceptions.PaymentNotFoundException;
import com.ramcharans.chipotle.model.Order;
import com.ramcharans.chipotle.model.Payment;
import com.ramcharans.chipotle.model.Payment.Type;
import com.ramcharans.chipotle.service.transaction.PaymentTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    PaymentDAO paymentDAO;

    @Autowired
    @Qualifier("cashPayment")
    PaymentTransactionService cashPaymentTransactionService;

    @Autowired
    @Qualifier("checkPayment")
    PaymentTransactionService checkPaymentTransactionService;

    @Autowired
    @Qualifier("giftCardPayment")
    PaymentTransactionService giftCardPaymentTransactionService;

    @Autowired
    @Qualifier("creditCardPayment")
    PaymentTransactionService creditCardPaymentTransactionService;

    public Order processPayment(Order order, Type paymentType, Map<String, String> paymentDetails) throws
            InsufficientPaymentDetailsException, PaymentFailedException {

        PaymentTransactionService paymentTransactionService = getPaymentTransactionServiceByType(paymentType);

        if (!paymentTransactionService.isValidPaymentDetails(paymentDetails))
            throw new InsufficientPaymentDetailsException("Payment details for type '" + paymentType +
                    "' must contain the following details: " + paymentTransactionService.getRequiredPaymentDetails());

        Payment payment = new Payment();

        payment.setId(paymentTransactionService.createId());
        payment.setType(paymentType);
        payment.setOrderId(order.getId());
        payment.setAmount(order.getTotal());

        try {
            paymentTransactionService.processTransaction(paymentDetails);
            payment.setIsSuccess(true);

            order.setPayment(payment);
            order.setPaid(true);

            return order;
        } catch (Exception e) {
            throw new PaymentFailedException();
        }
    }

    public void savePayment(Payment payment) {
        paymentDAO.savePaymentToDB(payment);
    }

    public Payment getPaymentById(String paymentId) throws PaymentNotFoundException {
        Optional<Payment> payment = paymentDAO.getPaymentById(paymentId);

        if (payment.isPresent())
            return payment.get();
        else
            throw new PaymentNotFoundException("Payment not found with id: " + paymentId);
    }

    private PaymentTransactionService getPaymentTransactionServiceByType(Type paymentType) {
        if (paymentType == Type.CASH) {
            return cashPaymentTransactionService;
        } else if (paymentType == Type.CHECK) {
            return checkPaymentTransactionService;
        } else if (paymentType == Type.CREDIT_CARD) {
            return creditCardPaymentTransactionService;
        } else if (paymentType == Type.GIFT_CARD) {
            return giftCardPaymentTransactionService;
        } else {
            throw new RuntimeException();
        }
    }
}

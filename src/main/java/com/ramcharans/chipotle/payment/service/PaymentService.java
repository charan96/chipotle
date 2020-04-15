package com.ramcharans.chipotle.payment.service;

import com.ramcharans.chipotle.payment.dao.PaymentDAO;
import com.ramcharans.chipotle.transaction.exceptions.InvalidPaymentDetailsException;
import com.ramcharans.chipotle.transaction.exceptions.PaymentTransactionFailedException;
import com.ramcharans.chipotle.payment.exceptions.PaymentNotFoundException;
import com.ramcharans.chipotle.order.model.Order;
import com.ramcharans.chipotle.payment.model.Payment;
import com.ramcharans.chipotle.payment.model.Payment.Type;
import com.ramcharans.chipotle.transaction.service.PaymentTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
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

    public Order processAndSavePayment(Order order, Type paymentType, Map<String, String> paymentDetails) throws
            InvalidPaymentDetailsException, PaymentTransactionFailedException {
        Order updatedOrder = processPayment(order, paymentType, paymentDetails);
        savePayment(updatedOrder.getPayment());

        return updatedOrder;
    }

    public Order processPayment(Order order, Type paymentType, Map<String, String> paymentDetails) throws
            InvalidPaymentDetailsException, PaymentTransactionFailedException {

        PaymentTransactionService paymentTransactionService = getPaymentTransactionServiceByType(paymentType);

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
        } catch (InvalidPaymentDetailsException e) {
            throw e;
        } catch (Exception e) {
            // FIXME: should catch a more specific exception in real life scenarios
            throw new PaymentTransactionFailedException();
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
        // FIXME: need to make this a little better
        EnumMap<Type, PaymentTransactionService> paymentTypeToTransactionServiceMap = new EnumMap<>(Type.class);

        paymentTypeToTransactionServiceMap.put(Type.CASH, cashPaymentTransactionService);
        paymentTypeToTransactionServiceMap.put(Type.CHECK, checkPaymentTransactionService);
        paymentTypeToTransactionServiceMap.put(Type.CREDIT_CARD, creditCardPaymentTransactionService);
        paymentTypeToTransactionServiceMap.put(Type.GIFT_CARD, giftCardPaymentTransactionService);

        return paymentTypeToTransactionServiceMap.get(paymentType);
    }
}
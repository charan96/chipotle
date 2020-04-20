package com.ramcharans.chipotle.payment.service;

import com.ramcharans.chipotle.order.exceptions.OrderNotFoundException;
import com.ramcharans.chipotle.order.service.OrderService;
import com.ramcharans.chipotle.payment.dao.PaymentDAO;
import com.ramcharans.chipotle.transaction.exceptions.InvalidPaymentDetailsException;
import com.ramcharans.chipotle.transaction.exceptions.PaymentTransactionFailedException;
import com.ramcharans.chipotle.payment.exceptions.PaymentNotFoundException;
import com.ramcharans.chipotle.order.model.Order;
import com.ramcharans.chipotle.payment.model.Payment;
import com.ramcharans.chipotle.payment.model.Payment.Type;
import com.ramcharans.chipotle.transaction.service.PaymentTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    OrderService orderService;

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

    public static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    public Payment processAndSavePayment(String orderId, Type paymentType, Map<String, String> paymentDetails) throws
            InvalidPaymentDetailsException, PaymentTransactionFailedException, OrderNotFoundException {
        Payment payment = processPayment(orderId, paymentType, paymentDetails);
        savePayment(payment);

        return payment;
    }

    public Payment processPayment(String orderId, Type paymentType, Map<String, String> paymentDetails) throws
            InvalidPaymentDetailsException, PaymentTransactionFailedException, OrderNotFoundException {

        Order order = orderService.findOrder(orderId);

        PaymentTransactionService paymentTransactionService = getPaymentTransactionServiceByType(paymentType);

        Payment payment = new Payment();

        payment.setId(paymentTransactionService.createId());
        payment.setType(paymentType);
        payment.setOrderId(orderId);
        payment.setAmount(order.getTotal());

        try {
            paymentTransactionService.processTransaction(paymentDetails);
            payment.setIsSuccess(true);

            order.setPayment(payment);
            order.setFulfilled(false);

            orderService.saveOrder(order);
            return payment;
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

    protected PaymentTransactionService getPaymentTransactionServiceByType(Type paymentType) {
        // FIXME: need to make this a little better
        EnumMap<Type, PaymentTransactionService> paymentTypeToTransactionServiceMap = new EnumMap<>(Type.class);

        paymentTypeToTransactionServiceMap.put(Type.CASH, cashPaymentTransactionService);
        paymentTypeToTransactionServiceMap.put(Type.CHECK, checkPaymentTransactionService);
        paymentTypeToTransactionServiceMap.put(Type.CREDIT_CARD, creditCardPaymentTransactionService);
        paymentTypeToTransactionServiceMap.put(Type.GIFT_CARD, giftCardPaymentTransactionService);

        return paymentTypeToTransactionServiceMap.get(paymentType);
    }
}
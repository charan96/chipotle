package com.ramcharans.chipotle.payment.controller;

import com.ramcharans.chipotle.order.exceptions.OrderNotFoundException;
import com.ramcharans.chipotle.transaction.exceptions.InvalidPaymentDetailsException;
import com.ramcharans.chipotle.transaction.exceptions.PaymentTransactionFailedException;
import com.ramcharans.chipotle.payment.exceptions.PaymentNotFoundException;
import com.ramcharans.chipotle.order.model.Order;
import com.ramcharans.chipotle.payment.model.Payment;
import com.ramcharans.chipotle.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @PostMapping(value = "/process", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> processPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            Order order = paymentService.processAndSavePayment(paymentRequest.getOrderId(),
                    paymentRequest.getPaymentType(), paymentRequest.getPaymentDetails());

            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (InvalidPaymentDetailsException e) {
            return new ResponseEntity<>("invalid payment details for payment processing", HttpStatus.BAD_REQUEST);
        } catch (PaymentTransactionFailedException e) {
            return new ResponseEntity<>("payment failed", HttpStatus.BAD_REQUEST);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(MessageFormat.format("order with ID '{0}' not found",
                    paymentRequest.getOrderId()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/find", produces = "application/json")
    public ResponseEntity<Object> findPaymentById(@RequestParam String id) {
        try {
            Payment payment = paymentService.getPaymentById(id);
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (PaymentNotFoundException e) {
            return new ResponseEntity<>(MessageFormat.format("payment not found with id: {0}", id),
                    HttpStatus.NOT_FOUND);
        }
    }
}

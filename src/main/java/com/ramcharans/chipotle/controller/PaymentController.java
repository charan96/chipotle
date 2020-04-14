package com.ramcharans.chipotle.controller;

import com.ramcharans.chipotle.controller.wrappers.PaymentRequestWrapper;
import com.ramcharans.chipotle.dao.PaymentDAO;
import com.ramcharans.chipotle.exceptions.InsufficientPaymentDetailsException;
import com.ramcharans.chipotle.exceptions.PaymentFailedException;
import com.ramcharans.chipotle.exceptions.PaymentNotFoundException;
import com.ramcharans.chipotle.model.Order;
import com.ramcharans.chipotle.model.Payment;
import com.ramcharans.chipotle.service.PaymentService;
import com.ramcharans.chipotle.model.Payment.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @PostMapping(value = "/process", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> processPayment(@RequestBody PaymentRequestWrapper paymentRequestWrapper) {
        try {
            Order order = paymentService.processPayment(paymentRequestWrapper.getOrder(),
                    paymentRequestWrapper.getPaymentType(), paymentRequestWrapper.getPaymentDetails());
            paymentService.savePayment(order.getPayment());

            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (InsufficientPaymentDetailsException | PaymentFailedException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/find", produces = "application/json")
    public ResponseEntity<Object> findPaymentById(@RequestParam String id) {
        try {
            Payment payment = paymentService.getPaymentById(id);
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (PaymentNotFoundException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        }
    }
}

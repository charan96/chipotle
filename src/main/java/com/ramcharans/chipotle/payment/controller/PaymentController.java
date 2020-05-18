package com.ramcharans.chipotle.payment.controller;

import com.ramcharans.chipotle.order.exceptions.OrderNotFoundException;
import com.ramcharans.chipotle.transaction.exceptions.InvalidPaymentDetailsException;
import com.ramcharans.chipotle.transaction.exceptions.PaymentTransactionFailedException;
import com.ramcharans.chipotle.payment.exceptions.PaymentNotFoundException;
import com.ramcharans.chipotle.payment.model.Payment;
import com.ramcharans.chipotle.payment.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@RestController
@Api(value = "Payment Management System")
@RequestMapping("/payment")
public class PaymentController {
    PaymentService paymentService;

    public static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @ApiOperation(value = "process the payment based on information provided", response = Payment.class)
    @PostMapping(value = "/process", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> processPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            Payment payment = paymentService.processAndSavePayment(paymentRequest.getOrderId(),
                    paymentRequest.getPaymentType(), paymentRequest.getPaymentDetails());

            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (InvalidPaymentDetailsException e) {
            return new ResponseEntity<>("invalid payment details for payment processing", HttpStatus.BAD_REQUEST);
        } catch (PaymentTransactionFailedException e) {
            return new ResponseEntity<>("payment failed", HttpStatus.BAD_REQUEST);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(MessageFormat.format("order with ID '{0}' not found",
                    paymentRequest.getOrderId()), HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "find a payment with given ID", response = Payment.class)
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

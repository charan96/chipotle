package com.ramcharans.chipotle.payment.controller;

import com.ramcharans.chipotle.order.exceptions.OrderNotFoundException;
import com.ramcharans.chipotle.order.model.Order;
import com.ramcharans.chipotle.payment.exceptions.PaymentNotFoundException;
import com.ramcharans.chipotle.payment.model.Payment;
import com.ramcharans.chipotle.payment.service.PaymentService;
import com.ramcharans.chipotle.transaction.exceptions.InvalidPaymentDetailsException;
import com.ramcharans.chipotle.transaction.exceptions.PaymentTransactionFailedException;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = PaymentController.class)
public class PaymentControllerTest {
    private final String APPLICATION_JSON = "application/json";

    @Autowired
    MockMvc mvc;

    @Mock
    PaymentService paymentService;

    @MockBean
    PaymentController paymentController;

    @Before
    public void setup() {
        paymentController = new PaymentController(paymentService);
        mvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    public void testProcessPaymentHandlesInvalidPaymentDetailsException() throws Exception {
        when(paymentService.processAndSavePayment(anyString(), eq(Payment.Type.CREDIT_CARD),
                eq(Collections.emptyMap())))
                .thenThrow(InvalidPaymentDetailsException.class);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId("id1");
        paymentRequest.setPaymentDetails(Collections.emptyMap());
        paymentRequest.setPaymentType(Payment.Type.CREDIT_CARD);

        mvc.perform(post("/payment/process")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(paymentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testProcessPaymentHandlesPaymentTransactionFailedException() throws Exception {
        when(paymentService.processAndSavePayment(anyString(), eq(Payment.Type.CREDIT_CARD),
                eq(Collections.emptyMap())))
                .thenThrow(PaymentTransactionFailedException.class);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId("id1");
        paymentRequest.setPaymentDetails(Collections.emptyMap());
        paymentRequest.setPaymentType(Payment.Type.CREDIT_CARD);

        mvc.perform(post("/payment/process")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(paymentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testProcessPaymentHandlesOrderNotFoundException() throws Exception {
        when(paymentService.processAndSavePayment(anyString(), eq(Payment.Type.CREDIT_CARD),
                eq(Collections.emptyMap())))
                .thenThrow(OrderNotFoundException.class);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId("id1");
        paymentRequest.setPaymentDetails(Collections.emptyMap());
        paymentRequest.setPaymentType(Payment.Type.CREDIT_CARD);

        mvc.perform(post("/payment/process")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(paymentRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testProcessPaymentReturnOkStatusForValidInput() throws Exception {
        when(paymentService.processAndSavePayment(anyString(), eq(Payment.Type.CREDIT_CARD),
                eq(Collections.emptyMap()))).thenReturn(new Payment());

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId("id1");
        paymentRequest.setPaymentDetails(Collections.emptyMap());
        paymentRequest.setPaymentType(Payment.Type.CREDIT_CARD);

        mvc.perform(post("/payment/process")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(paymentRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindPaymentByIdHandlesPaymentNotFoundException() throws Exception {
        when(paymentService.getPaymentById(anyString())).thenThrow(PaymentNotFoundException.class);

        mvc.perform(get("/payment/find")
                .contentType(APPLICATION_JSON)
                .param("id", "id1"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testFindPaymentByIdReturnsOkForValidId() throws Exception {
        when(paymentService.getPaymentById(anyString())).thenReturn(new Payment());
        mvc.perform(get("/payment/find")
                .contentType(APPLICATION_JSON).param("id", "id1"))
                .andExpect(status().isOk());
    }
}

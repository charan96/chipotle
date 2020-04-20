package com.ramcharans.chipotle.payment.service;

import com.ramcharans.chipotle.order.exceptions.OrderNotFoundException;
import com.ramcharans.chipotle.order.service.OrderService;
import com.ramcharans.chipotle.payment.dao.PaymentDAO;
import com.ramcharans.chipotle.payment.exceptions.PaymentNotFoundException;
import com.ramcharans.chipotle.payment.model.Payment;
import com.ramcharans.chipotle.transaction.exceptions.InvalidPaymentDetailsException;
import com.ramcharans.chipotle.transaction.exceptions.PaymentTransactionFailedException;
import com.ramcharans.chipotle.transaction.service.*;
import com.ramcharans.chipotle.order.model.*;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Test;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {
    @Mock
    PaymentDAO paymentDAO;

    @Mock
    OrderService orderService;

    @Mock
    @Qualifier("cashPayment")
    PaymentTransactionService cash;

    @Mock
    @Qualifier("checkPayment")
    PaymentTransactionService check;

    @Mock
    @Qualifier("giftCardPayment")
    PaymentTransactionService gc;

    @Mock
    @Qualifier("creditCardPayment")
    PaymentTransactionService cc;

    @InjectMocks
    PaymentService paymentService;

    PaymentService spy;

    @Before
    public void init() {
        spy = spy(paymentService);
    }

    @Test(expected = PaymentNotFoundException.class)
    public void testGetPaymentByIdThrowsExceptionWhenPaymentNotFound() throws PaymentNotFoundException {
        when(paymentDAO.getPaymentById("id1")).thenReturn(Optional.empty());

        spy.getPaymentById("id1");
    }

    @Test
    public void testGetPaymentByIdReturnsCorrectValueWithValidInput() throws PaymentNotFoundException {
        Payment payment = mock(Payment.class);
        when(paymentDAO.getPaymentById("id1")).thenReturn(Optional.of(payment));

        assert spy.getPaymentById("id1").equals(payment);
    }

    @Test
    public void testSavePaymentCallsPaymentDAOSavePayment() {
        Payment payment = mock(Payment.class);
        spy.savePayment(payment);

        verify(paymentDAO).savePaymentToDB(payment);
    }

    @Test(expected = OrderNotFoundException.class)
    public void testProcessPaymentFailsWhenOrderIdNotFound() throws OrderNotFoundException,
            PaymentTransactionFailedException, InvalidPaymentDetailsException {
        when(orderService.findOrder("id1")).thenThrow(OrderNotFoundException.class);

        spy.processPayment("id1", Payment.Type.CREDIT_CARD, Collections.singletonMap("id4", "id5"));
    }

    @Test(expected = InvalidPaymentDetailsException.class)
    public void testProcessPaymentFailsWhenInvalidPaymentDetailsExceptionIsThrown() throws InvalidPaymentDetailsException, OrderNotFoundException, PaymentTransactionFailedException {
        Order order = mock(Order.class);
        when(orderService.findOrder("id1")).thenReturn(order);

        doThrow(InvalidPaymentDetailsException.class).when(cc).processTransaction(
                Collections.singletonMap("id4", "id5"));

        doReturn(cc).when(spy).getPaymentTransactionServiceByType(Payment.Type.CREDIT_CARD);
        spy.processPayment("id1", Payment.Type.CREDIT_CARD, Collections.singletonMap("id4", "id5"));
    }

    @Test(expected = PaymentTransactionFailedException.class)
    public void testProcessPaymentFailsWhenPaymentFails() throws InvalidPaymentDetailsException,
            OrderNotFoundException, PaymentTransactionFailedException {
        Order order = mock(Order.class);
        when(orderService.findOrder("id1")).thenReturn(order);

        doThrow(RuntimeException.class).when(cc).processTransaction(Collections.singletonMap("id4", "id5"));
        doReturn(cc).when(spy).getPaymentTransactionServiceByType(Payment.Type.CREDIT_CARD);

        spy.processPayment("id1", Payment.Type.CREDIT_CARD, Collections.singletonMap("id4", "id5"));
    }

    @Test
    public void testProcessPaymentReturnsCorrectValue() throws OrderNotFoundException, InvalidPaymentDetailsException,
            PaymentTransactionFailedException {
        Order order = mock(Order.class);
        Payment payment = mock(Payment.class);
        when(orderService.findOrder("id1")).thenReturn(order);

        doReturn(cc).when(spy).getPaymentTransactionServiceByType(Payment.Type.CREDIT_CARD);

        assertThat(spy.processPayment("id1", Payment.Type.CREDIT_CARD, Collections.singletonMap("id4", "id5")),
                instanceOf(Payment.class));
    }

    @Test
    public void testProcessAndSavePaymentCallsProcessPayment() throws InvalidPaymentDetailsException,
            OrderNotFoundException, PaymentTransactionFailedException {
        Order order = mock(Order.class);
        Payment payment = mock(Payment.class);

        // when(order.getPayment()).thenReturn(payment);

        doReturn(payment).when(spy).processPayment("id1", Payment.Type.CREDIT_CARD, Collections.singletonMap("id4",
                "id5"));
        doNothing().when(spy).savePayment(payment);

        spy.processAndSavePayment("id1", Payment.Type.CREDIT_CARD, Collections.singletonMap("id4", "id5"));

        verify(spy).processPayment("id1", Payment.Type.CREDIT_CARD, Collections.singletonMap("id4", "id5"));
    }

    @Test
    public void testProcessAndSavePaymentCallsSavePayment() throws InvalidPaymentDetailsException,
            OrderNotFoundException, PaymentTransactionFailedException {
        Order order = mock(Order.class);
        Payment payment = mock(Payment.class);

        // when(order.getPayment()).thenReturn(payment);

        doReturn(payment).when(spy).processPayment("id1", Payment.Type.CREDIT_CARD, Collections.singletonMap("id4",
                "id5"));
        doNothing().when(spy).savePayment(payment);

        spy.processAndSavePayment("id1", Payment.Type.CREDIT_CARD, Collections.singletonMap("id4", "id5"));

        verify(spy).savePayment(payment);
    }
}

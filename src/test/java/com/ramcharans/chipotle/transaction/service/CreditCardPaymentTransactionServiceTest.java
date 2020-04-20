package com.ramcharans.chipotle.transaction.service;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.spy;

public class CreditCardPaymentTransactionServiceTest {
    CreditCardPaymentTransactionService svc;

    CreditCardPaymentTransactionService spy;

    @Before
    public void init() {
        svc = new CreditCardPaymentTransactionService();
        spy = spy(svc);
    }

    @Test
    public void testCreateIdMakesCorrectId() {
        assert spy.createId().startsWith("CC");
    }
}

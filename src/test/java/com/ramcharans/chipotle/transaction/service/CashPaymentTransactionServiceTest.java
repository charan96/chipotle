package com.ramcharans.chipotle.transaction.service;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.spy;

public class CashPaymentTransactionServiceTest {
    CashPaymentTransactionService svc;

    CashPaymentTransactionService spy;

    @Before
    public void init() {
        svc = new CashPaymentTransactionService();
        spy = spy(svc);
    }

    @Test
    public void testCreateIdMakesCorrectId() {
        assert spy.createId().startsWith("CS");
    }
}

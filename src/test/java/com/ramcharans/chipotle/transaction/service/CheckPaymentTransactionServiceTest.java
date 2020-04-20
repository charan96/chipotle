package com.ramcharans.chipotle.transaction.service;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.spy;

public class CheckPaymentTransactionServiceTest {
    CheckPaymentTransactionService svc;

    CheckPaymentTransactionService spy;

    @Before
    public void init() {
        svc = new CheckPaymentTransactionService();
        spy = spy(svc);
    }

    @Test
    public void testCreateIdMakesCorrectId() {
        assert spy.createId().startsWith("CK");
    }
}

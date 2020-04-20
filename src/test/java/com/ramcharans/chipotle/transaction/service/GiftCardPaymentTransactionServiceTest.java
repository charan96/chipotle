package com.ramcharans.chipotle.transaction.service;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GiftCardPaymentTransactionServiceTest {
    GiftCardPaymentTransactionService svc;

    GiftCardPaymentTransactionService spy;

    @Before
    public void init() {
        svc = new GiftCardPaymentTransactionService();
        spy = spy(svc);
    }

    @Test
    public void testCreateIdMakesCorrectId() {
        assert spy.createId().startsWith("GC");
    }
}

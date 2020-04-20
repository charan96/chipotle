package com.ramcharans.chipotle.payment.dao;

import com.ramcharans.chipotle.payment.model.Payment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PaymentDAOTest {
    @Mock
    MongoTemplate mongoTemplate;

    @InjectMocks
    PaymentDAO paymentDAO;

    PaymentDAO spy;

    @Before
    public void init() {
        // note: spy lets you do a partial mock; so that if you want to mock certain methods, you can; for the others,
        // note contd: it'll run the real method
        spy = spy(paymentDAO);
    }

    @Test
    public void testSavePaymentToDBCallsMongoTemplateSave() {
        Payment payment = mock(Payment.class);

        spy.savePaymentToDB(payment);

        verify(mongoTemplate).save(payment);
    }

    @Test
    public void testGetPaymentByIdReturnsNullWhenIdIsInvalid() {
        when(mongoTemplate.findOne(any(Query.class), eq(Payment.class))).thenReturn(null);

        assert !spy.getPaymentById("id1").isPresent();
    }

    @Test
    public void testGetPaymentByIdReturnsValueWithValidInput() {
        when(mongoTemplate.findOne(any(Query.class), eq(Payment.class))).thenReturn(mock(Payment.class));

        assert spy.getPaymentById("id1").isPresent();
    }
}

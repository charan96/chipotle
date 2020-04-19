package com.ramcharans.chipotle.order.dao;

import com.ramcharans.chipotle.order.model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderDAOTest {
    @Mock
    MongoTemplate mongoTemplate;

    @InjectMocks
    OrderDAO orderDAO;

    OrderDAO spy;

    @Before
    public void init() {
        spy = spy(orderDAO);
    }

    @Test
    public void testGetAllOrdersCallsMongoTemplateFindAll() {
        when(mongoTemplate.findAll(Order.class)).thenReturn(Collections.emptyList());

        spy.getAllOrders();

        verify(mongoTemplate).findAll(Order.class);
    }

    @Test
    public void testSaveOrderCallsMongoTemplateSave() {
        Order order = mock(Order.class);
        when(mongoTemplate.save(order)).thenReturn(order);

        spy.saveOrder(order);

        verify(mongoTemplate).save(order);
    }

    @Test
    public void testFindByIdReturnsEmptyOptionalWhenIdDoesNotExist() {
        when(mongoTemplate.findOne(any(Query.class), eq(Order.class))).thenReturn(null);
        assertFalse(spy.findById("id").isPresent());
    }

    @Test
    public void testFindByIdReturnValidOptionalWhenMongoTemplateReturnsValue() {
        when(mongoTemplate.findOne(any(Query.class), eq(Order.class))).thenReturn(mock(Order.class));
        assertTrue(spy.findById("id").isPresent());
    }

    @Test
    public void testFindByIsFulfilledCallsFindOnMongoTemplate() {
        when(mongoTemplate.find(any(Query.class), eq(Order.class))).thenReturn(Collections.emptyList());

        spy.findByIsFulfilled(true);

        verify(mongoTemplate).find(any(Query.class), eq(Order.class));
    }

    @Test
    public void testFindByCustomerIdCallsFindOnMongoTemplate() {
        when(mongoTemplate.find(any(Query.class), eq(Order.class))).thenReturn(Collections.emptyList());

        spy.findByCustomerId("customerid1");

        verify(mongoTemplate).find(any(Query.class), eq(Order.class));
    }
}

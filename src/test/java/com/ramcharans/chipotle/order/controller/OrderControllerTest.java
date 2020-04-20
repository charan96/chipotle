package com.ramcharans.chipotle.order.controller;

import java.util.*;

import com.ramcharans.chipotle.ingredient.exceptions.IngredientNotFoundException;
import com.ramcharans.chipotle.order.exceptions.OrderNotFoundException;
import com.ramcharans.chipotle.order.model.Order;
import com.ramcharans.chipotle.order.service.OrderService;
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

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = OrderController.class)
public class OrderControllerTest {
    private final String APPLICATION_JSON = "application/json";

    @Autowired
    MockMvc mvc;

    @Mock
    OrderService orderService;

    @MockBean
    OrderController orderController;

    @Before
    public void setup() {
        orderController = new OrderController();
        orderController.orderService = orderService;

        mvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void testGetAllOrdersReturnsOk() throws Exception {
        when(orderService.getAllOrders()).thenReturn(null);

        mvc.perform(get("/order/")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testSubmitOrderThrowsExceptionWhenIngredientNotFound() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId("id1");
        orderRequest.setIngredientIds(Arrays.asList("id1", "id2"));

        when(orderService.buildAndSaveOrder(anyString(), eq(Arrays.asList("id1", "id2"))))
                .thenThrow(IngredientNotFoundException.class);

        mvc.perform(post("/order/submit")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSubmitOrderReturnsCorrectObjectWithValidValue() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId("cust1");
        orderRequest.setIngredientIds(Arrays.asList("id1", "id2"));

        Order order = mock(Order.class);
        when(order.getCustomerId()).thenReturn("cust1");

        when(orderService.buildAndSaveOrder(anyString(), eq(Arrays.asList("id1", "id2"))))
                .thenReturn(order);

        mvc.perform(post("/order/submit")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(jsonPath("$['customerId']", is("cust1")))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindOrderByIdThrowsOrderNotFoundExceptionWhenIdIsInvalid() throws Exception {
        when(orderService.findOrder("id1")).thenThrow(OrderNotFoundException.class);

        mvc.perform(get("/order/find/id")
                .contentType(APPLICATION_JSON)
                .param("id", "id1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindOrderByIdReturnsStatusOkForValidStatus() throws Exception {
        Order order = mock(Order.class);
        when(orderService.findOrder("id1")).thenReturn(null);

        mvc.perform(get("/order/find/id")
                .contentType(APPLICATION_JSON)
                .param("id", "id1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindOrderByIsFulfilledReturnsStatusOk() throws Exception {
        when(orderService.findOrdersByIsFulfilled(anyBoolean())).thenReturn(Collections.emptyList());

        mvc.perform(get("/order/find/fulfilled")
                .contentType(APPLICATION_JSON)
                .param("fulfilled", "true"))
                .andExpect(status().isOk());
    }
}

package com.ramcharans.chipotle.order.service;

import com.ramcharans.chipotle.order.dao.OrderDAO;
import com.ramcharans.chipotle.order.exceptions.OrderNotFoundException;
import com.ramcharans.chipotle.order.model.Order;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Test;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {
    // @Mock
    // OrderDAO orderDAO;
    //
    // @Mock
    // IngredientService ingredientService;
    //
    // @InjectMocks
    // OrderService orderService;
    //
    // OrderService spy;
    //
    // @Before
    // public void init() {
    //     spy = spy(orderService);
    // }
    //
    // @Test
    // public void testBuildAndSaveOrderCallsBuildOrderAndSaveOrder() throws IngredientNotFoundException {
    //     Order order = mock(Order.class);
    //     doReturn(order).when(spy).buildOrder("id1", Collections.emptyList());
    //     doNothing().when(spy).saveOrder(order);
    //
    //     spy.buildAndSaveOrder("id1", Collections.emptyList());
    //
    //     verify(spy).buildAndSaveOrder("id1", Collections.emptyList());
    //     verify(spy).saveOrder(order);
    // }
    //
    // @Test(expected = IngredientNotFoundException.class)
    // public void testBuildOrderThrowsExceptionWhenIngredientsAreNotValidated() throws IngredientNotFoundException {
    //     when(ingredientService.getIngredientById(anyString())).thenReturn(Optional.empty());
    //
    //     spy.buildOrder("id1", Arrays.asList("ing1", "ing2"));
    // }
    //
    // @Test
    // public void testSaveOrderCallsDAOSaveOrder() {
    //     Order order = mock(Order.class);
    //     spy.saveOrder(order);
    //
    //     verify(orderDAO).saveOrder(order);
    // }
    //
    // @Test
    // public void testGetAllOrdersCallsDAOGetAllOrders() {
    //     spy.getAllOrders();
    //     verify(orderDAO).getAllOrders();
    // }
    //
    // @Test(expected = OrderNotFoundException.class)
    // public void testFindOrderThrowsOrderNotFoundExceptionForInvalidId() throws OrderNotFoundException {
    //     when(orderDAO.findById("id")).thenReturn(Optional.empty());
    //
    //     spy.findOrder("id");
    //
    // }
    //
    // @Test
    // public void testFindOrderReturnsCorrectValueForValidId() throws OrderNotFoundException {
    //     Order order = mock(Order.class);
    //     when(orderDAO.findById("id")).thenReturn(Optional.of(order));
    //
    //     assert spy.findOrder("id").equals(order);
    // }
    //
    // @Test
    // public void testFindOrdersByIsFulfilledCallsOrderDAOFindByIsFulfilled() {
    //     when(orderDAO.findByIsFulfilled(anyBoolean())).thenReturn(Collections.emptyList());
    //     spy.findOrdersByIsFulfilled(true);
    //
    //     verify(spy).findOrdersByIsFulfilled(true);
    // }
    //
    // @Test(expected = IngredientNotFoundException.class)
    // public void testGetIngredientsListFromIngredientsIdsThrowsExceptionWhenIngredientIdDoesNotExist()
    //         throws IngredientNotFoundException {
    //     when(ingredientService.getIngredientById(anyString())).thenReturn(Optional.empty());
    //
    //     spy.getIngredientsListFromIngredientIds(Arrays.asList("id1", "id2"));
    // }
    //
    // @Test
    // public void testGetIngredientsListFromIngredientsIdsReturnsCorrectValues() throws IngredientNotFoundException {
    //     Ingredient ing1 = mock(Ingredient.class);
    //     Ingredient ing2 = mock(Ingredient.class);
    //
    //     when(ingredientService.getIngredientById("id1")).thenReturn(Optional.of(ing1));
    //     when(ingredientService.getIngredientById("id2")).thenReturn(Optional.of(ing2));
    //
    //     List<Ingredient> ings = spy.getIngredientsListFromIngredientIds(Arrays.asList("id1", "id2"));
    //
    //     assert ings.equals(Arrays.asList(ing1, ing2));
    // }
    //
    // @Test
    // public void testCalculateOrderTotalCalculatesCorrectPrice()
    // {
    //     // note: too much of a pain right now; most likely going to need a test db for this
    //     assert true;
    // }
}



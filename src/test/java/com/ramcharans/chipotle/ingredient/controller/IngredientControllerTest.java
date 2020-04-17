package com.ramcharans.chipotle.ingredient.controller;

import com.ramcharans.chipotle.ingredient.model.Ingredient;
import com.ramcharans.chipotle.ingredient.service.IngredientsService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class IngredientControllerTest {
    @Mock
    IngredientsService ingredientsService;

    @InjectMocks
    IngredientController ingredientController;

//    @BeforeEach
//    public void setUpMockRequest() {
//
//    }

    @Test
    public void testGetAvailableIngredientsReturnsResponseEntityWithCorrectValues() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Ingredient ing1 = mock(Ingredient.class);
        Ingredient ing2 = mock(Ingredient.class);

        when(ingredientsService.getAvailableIngredients()).thenReturn(Arrays.asList(ing1, ing2));

        ResponseEntity<Object> response = ingredientController.getAvailableIngredients();

        assertEquals(response.getBody(), Arrays.asList(ing1, ing2));
    }

    @Test
    public void testGetAvailableIngredientsReturnsCorrectResponseCode() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Ingredient ing1 = mock(Ingredient.class);
        Ingredient ing2 = mock(Ingredient.class);

        when(ingredientsService.getAvailableIngredients()).thenReturn(Arrays.asList(ing1, ing2));

        ResponseEntity<Object> response = ingredientController.getAvailableIngredients();

        assertEquals(response.getStatusCode().value(), 200);
    }

}


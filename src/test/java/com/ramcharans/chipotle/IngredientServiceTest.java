package com.ramcharans.chipotle;

import com.ramcharans.chipotle.ingredient.dao.IngredientsDAO;
import com.ramcharans.chipotle.ingredient.model.Ingredient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
//@WebMvcTest(IngredientController.class)
public class IngredientServiceTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    IngredientsDAO ingredientsDAO;

    @Test
    public void testIngredientDAONotNull() {
        assertNotNull(ingredientsDAO);
    }

    @Test
    public void testListAvailableIngredients() {
        Ingredient i1 = mock(Ingredient.class);
        Ingredient i2 = mock(Ingredient.class);

        List<Ingredient> ilist = Arrays.asList(i1, i2);

        when(ingredientsDAO.getAvailableIngredients()).thenReturn(ilist);


    }
}

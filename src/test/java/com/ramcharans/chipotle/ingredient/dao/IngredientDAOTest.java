package com.ramcharans.chipotle.ingredient.dao;

import com.ramcharans.chipotle.ingredient.exceptions.ValueExistsInListException;
import com.ramcharans.chipotle.ingredient.model.Ingredient;
import junit.runner.Version;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IngredientDAOTest {
    @Mock
    IngredientsDAO ingredientsDAO;

    @Test(expected = ValueExistsInListException.class)
    public void testAddIngredientToAvailableIngredientsThrowsValueExistsInListExceptionWhenIngredientAlreadyExists() throws
            ValueExistsInListException {
        Ingredient ing = mock(Ingredient.class);
        when(ing.getId()).thenReturn(1L);
        when(ing.getType()).thenReturn(Ingredient.Type.MEAT);

        List<Ingredient> ings = new ArrayList<>();
        ings.add(ing);

        when(ingredientsDAO.getAvailableIngredients()).thenReturn(ings);
        doCallRealMethod().when(ingredientsDAO).addIngredientToAvailableIngredients(ing);

        ingredientsDAO.addIngredientToAvailableIngredients(ing);
    }
}
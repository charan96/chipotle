package com.ramcharans.chipotle.ingredient.service;

import com.mongodb.MongoClientException;
import com.ramcharans.chipotle.ingredient.dao.IngredientDAO;
import com.ramcharans.chipotle.ingredient.exceptions.FailedToAddIngredientException;
import com.ramcharans.chipotle.ingredient.exceptions.IngredientAlreadyExistsException;
import com.ramcharans.chipotle.ingredient.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Test;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IngredientServiceTest {
    @Mock
    private IngredientDAO ingredientDAO;

    @InjectMocks
    private IngredientsService ingredientsService;

    @BeforeEach
    public void init() {
        ingredientsService = new IngredientsService();
    }

    @Test
    public void testIngredientsDAOIsNotNull() {
        assertNotNull(ingredientsService.ingredientsDAO);
    }

    @Test
    public void testGetAllIngredientsReturnRightValues() {
        Ingredient i1 = mock(Ingredient.class);
        Ingredient i2 = mock(Ingredient.class);

        List<Ingredient> ings = Arrays.asList(i1, i2);

        when(ingredientDAO.getAllIngredients()).thenReturn(ings);

        assertEquals(ings, ingredientsService.getAvailableIngredients());
    }

    @Test
    public void testAddIngredientSucceedsWhenValidIngredientIsPassed() throws IngredientAlreadyExistsException,
            FailedToAddIngredientException {
        Ingredient ing = new Ingredient("rice1", Ingredient.Type.RICE, 0.0);

        doNothing().when(ingredientDAO).safeAddIngredient(ing);
        when(ingredientDAO.findByName("rice1")).thenReturn(Optional.of(ing));

        ingredientsService.addIngredient(ing);
    }

    @Test(expected = IngredientAlreadyExistsException.class)
    public void testAddIngredientsFailsWhenExistingIngredientIsPassed() throws IngredientAlreadyExistsException,
            FailedToAddIngredientException {
        Ingredient ing = mock(Ingredient.class);

        doThrow(IngredientAlreadyExistsException.class).when(ingredientDAO).safeAddIngredient(ing);
        ingredientsService.addIngredient(ing);
    }

    @Test(expected = FailedToAddIngredientException.class)
    public void testAddIngredientsFailsWhenIngredientAdditionFails() throws IngredientAlreadyExistsException,
            FailedToAddIngredientException {
        Ingredient ing = mock(Ingredient.class);

        doThrow(MongoClientException.class).when(ingredientDAO).safeAddIngredient(ing);
        ingredientsService.addIngredient(ing);
    }

    @Test
    public void testGetIngredientByIdReturnsObjectWhenPassedExistingIngredient() {
        List<Ingredient> ings = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Ingredient ing = mock(Ingredient.class);
            ings.add(ing);
        }

        when(ingredientDAO.findById("1")).thenReturn(java.util.Optional.ofNullable(ings.get(1)));

        assertEquals(ingredientsService.getIngredientById("1").get(), ings.get(1));
    }

    @Test(expected = IngredientAlreadyExistsException.class)
    public void testAddAllIngredientsThrowsExceptionIfIngredientIsAlreadyPresent() throws IngredientAlreadyExistsException, FailedToAddIngredientException {
        List<Ingredient> ings = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Ingredient ing = mock(Ingredient.class);
            when(ing.getId()).thenReturn(Integer.toString(i));
            ings.add(ing);
        }

        when(ingredientDAO.findById("2")).thenReturn(Optional.of(mock(Ingredient.class)));

        ingredientsService.addAllIngredients(ings);
    }

    @Test(expected = FailedToAddIngredientException.class)
    public void testAddAllIngredientsThrowsRightExceptionWhenAddIngredientFails() throws FailedToAddIngredientException, IngredientAlreadyExistsException {
        List<Ingredient> ings = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Ingredient ing = mock(Ingredient.class);
            when(ing.getId()).thenReturn(Integer.toString(i));
            ings.add(ing);
        }

        doThrow(MongoClientException.class).when(ingredientDAO).addIngredient(ings.get(2));

        ingredientsService.addAllIngredients(ings);
    }
}

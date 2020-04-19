package com.ramcharans.chipotle.ingredient.dao;

import com.ramcharans.chipotle.ingredient.exceptions.IngredientAlreadyExistsException;
import com.ramcharans.chipotle.ingredient.exceptions.IngredientNotFoundException;
import com.ramcharans.chipotle.ingredient.model.Ingredient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.class)
public class IngredientDAOTest {
    @Mock
    MongoTemplate mongoTemplate;

    @InjectMocks
    IngredientDAO ingredientDAO;

    IngredientDAO spy;

    @Before
    public void init() {
        // note: spy lets you do a partial mock; so that if you want to mock certain methods, you can; for the others,
        // note contd: it'll run the real method
        spy = spy(ingredientDAO);
    }

    @Test
    public void testGetAllIngredientsReturnsCorrectValues() {
        when(mongoTemplate.findAll(Ingredient.class)).thenReturn(Arrays.asList(mock(Ingredient.class),
                mock(Ingredient.class)));

        assert spy.getAllIngredients().size() == 2;
    }

    @Test
    public void testAddIngredientCallsMongoTemplateSaveMethod() {
        Ingredient ing = mock(Ingredient.class);
        spy.addIngredient(ing);

        verify(mongoTemplate).save(ing);
    }

    @Test(expected = IngredientNotFoundException.class)
    public void testDeleteIngredientForInvalidIdRaisesRightException() throws IngredientNotFoundException {
        doReturn(Optional.empty()).when(spy).findById("hello");
        spy.deleteIngredient("hello");
    }

    @Test(expected = IngredientAlreadyExistsException.class)
    public void testSafeAddIngredientFailsIfIngredientAlreadyExists() throws IngredientAlreadyExistsException {
        Ingredient ing = mock(Ingredient.class);
        when(ing.getName()).thenReturn("ing");

        doReturn(Optional.of("hello")).when(spy).findByName("ing");

        spy.safeAddIngredient(ing);
    }

    @Test
    public void testSafeAddIngredientReturnsNewlyCreatedIngredientId() throws IngredientAlreadyExistsException {
        Ingredient ing = mock(Ingredient.class);
        when(ing.getName()).thenReturn("new_ing");

        doReturn(Optional.empty()).when(spy).findByName("new_ing");
        doNothing().when(spy).addIngredient(ing);

        spy.safeAddIngredient(ing);

        verify(spy).addIngredient(ing);
    }
}
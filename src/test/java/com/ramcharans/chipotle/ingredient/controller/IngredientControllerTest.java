package com.ramcharans.chipotle.ingredient.controller;

import java.util.*;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramcharans.chipotle.ingredient.model.Ingredient;
import com.ramcharans.chipotle.ingredient.exceptions.FailedToAddIngredientException;
import com.ramcharans.chipotle.ingredient.exceptions.IngredientAlreadyExistsException;
import com.ramcharans.chipotle.ingredient.exceptions.IngredientNotFoundException;
import com.ramcharans.chipotle.ingredient.service.IngredientService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.collection.IsCollectionWithSize.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = IngredientController.class)
public class IngredientControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Mock
    IngredientService ingredientService;

    @MockBean
    IngredientController ingredientController;

    @Before
    public void setup() {
        ingredientController = new IngredientController();
        ingredientController.ingredientService = ingredientService;

        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    public void testGetAvailableIngredientsValidInputsReturns200Status() throws Exception {
        mockMvc.perform(get("/ingredients")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAvailableIngredientsValidInputReturnsCorrectData() throws Exception {
        List<Ingredient> ings = Arrays.asList(new Ingredient("rice1", Ingredient.Type.RICE, 0.0), new Ingredient(
                "rice2", Ingredient.Type.RICE, 0.0));

        when(ingredientService.getAvailableIngredients()).thenReturn(ings);

        mockMvc.perform(get("/ingredients")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetAvailableIngredientsCallsIngredientServiceGetAvailableIngredients() throws Exception {
        mockMvc.perform(get("/ingredients")
                .contentType("application/json"))
                .andExpect(status().isOk());

        verify(ingredientService).getAvailableIngredients();
    }

    @Test
    public void testAddIngredientThrowsExceptionWhenServiceThrowsException() throws Exception {
        Ingredient ing = mock(Ingredient.class);

        when(ingredientService.addIngredient(ing)).thenThrow(IngredientAlreadyExistsException.class);

        mockMvc.perform(post("/ingredients/add")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddIngredientReturnsStringIdAndOkStatus() throws Exception {
        Ingredient ing = new Ingredient("rice1", Ingredient.Type.RICE, 0.0);

        when(ingredientService.addIngredient(ing)).thenReturn("newid");

        mockMvc.perform(post("/ingredients/add")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(ing)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("newid")));
        // .andExpect(jsonPath("$['new_ingredient_id']", is("newid")));
    }

    @Test
    public void testAddIngredientReturnsInternalServerError() throws Exception {
        Ingredient ing = new Ingredient("rice1", Ingredient.Type.RICE, 0.0);

        when(ingredientService.addIngredient(ing)).thenThrow(FailedToAddIngredientException.class);

        mockMvc.perform(post("/ingredients/add")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(ing)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAddManyIngredientsReturnsOKForValidInput() throws Exception {
        List<Ingredient> ings = Arrays.asList(new Ingredient("rice1", Ingredient.Type.RICE, 0.0),
                new Ingredient("banzo", Ingredient.Type.BEANS, 0.0));

        doNothing().when(ingredientService).addAllIngredients(ings);

        mockMvc.perform(post("/ingredients/addMany")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(ings)))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddManyIngredientsThrowsBadRequestWhenIngredientExists() throws Exception {
        List<Ingredient> ings = Arrays.asList(new Ingredient("rice1", Ingredient.Type.RICE, 0.0),
                new Ingredient("banzo", Ingredient.Type.BEANS, 0.0));

        doThrow(IngredientAlreadyExistsException.class).when(ingredientService).addAllIngredients(ings);

        mockMvc.perform(post("/ingredients/addMany")
                .contentType("application/json").content(new ObjectMapper().writeValueAsString(ings)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddManyIngredientsThrowsInternalErrorWhenIngredientAdditionFails() throws Exception {
        List<Ingredient> ings = Arrays.asList(new Ingredient("rice1", Ingredient.Type.RICE, 0.0),
                new Ingredient("banzo", Ingredient.Type.BEANS, 0.0));

        doThrow(FailedToAddIngredientException.class).when(ingredientService).addAllIngredients(ings);

        mockMvc.perform(post("/ingredients/addMany")
                .contentType("application/json").content(new ObjectMapper().writeValueAsString(ings)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testFindIngredientByIdReturnsNotFoundWhenIngredientNotAvailable() throws Exception {
        when(ingredientService.getIngredientById("id1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/ingredients/filter/id/id1")
                .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindIngredientByIdReturnsRightValueWhenIdExists() throws Exception {
        when(ingredientService.getIngredientById("id1")).thenReturn(Optional.of(
                new Ingredient("rice1", Ingredient.Type.RICE, 0.0)));

        mockMvc.perform(get("/ingredients/filter/id/id1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['name']", is("rice1")));
    }

    @Test
    public void testFindByTypeReturnsNotFoundIfNoIngredientsOfTypeFound() throws Exception {
        when(ingredientService.getIngredientsByType(Ingredient.Type.BEANS)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ingredients/filter/type/BEANS")
                .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByTypeReturnsActualValuesListWhenIngredientsFound() throws Exception {
        List<Ingredient> ings = Arrays.asList(new Ingredient("rice1", Ingredient.Type.RICE, 0.0),
                new Ingredient("rice2", Ingredient.Type.RICE, 0.0));

        when(ingredientService.getIngredientsByType(Ingredient.Type.BEANS)).thenReturn(ings);

        mockMvc.perform(get("/ingredients/filter/type/BEANS")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(2)));
    }

    @Test
    public void testDeleteIngredientFailsWhenIdIsNotFound() throws Exception {
        doThrow(IngredientNotFoundException.class).when(ingredientService).deleteIngredient("id1");

        mockMvc.perform(delete("/ingredients/delete/id/id1")
                .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteIngredientReturnsOkWhenIdExists() throws Exception {
        doNothing().when(ingredientService).deleteIngredient("id1");

        mockMvc.perform(delete("/ingredients/delete/id/id1")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
}


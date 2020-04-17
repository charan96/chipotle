package com.ramcharans.chipotle.ingredient.service;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IngredientServiceTest {
    // @Mock
    // private OldIngredientsDAO ingredientsDAO;
    //
    // @InjectMocks
    // private IngredientsService ingredientsService;
    //
    // @BeforeEach
    // public void init() {
    //     ingredientsService = new IngredientsService();
    // }
    //
    // @Test
    // public void testIngredientsDAOIsNotNull() {
    //     assertNotNull(ingredientsService.ingredientsDAO);
    // }
    //
    // @Test
    // public void testGetAllIngredientsReturnRightValues() {
    //     Ingredient i1 = mock(Ingredient.class);
    //     Ingredient i2 = mock(Ingredient.class);
    //
    //     List<Ingredient> ings = Arrays.asList(i1, i2);
    //
    //     when(ingredientsDAO.getAvailableIngredients()).thenReturn(ings);
    //
    //     assertEquals(ings, ingredientsService.getAvailableIngredients());
    // }
    //
    // @Test
    // public void testAddIngredientSucceedsWhenValidIngredientIsPassed() {
    //     Ingredient ing = mock(Ingredient.class);
    //
    //     try {
    //         doNothing().when(ingredientsDAO).addIngredientToAvailableIngredients(ing);
    //
    //         ingredientsService.addIngredient(ing);
    //         assert true;
    //     } catch (Exception e) {
    //         assert false;
    //     }
    // }
    //
    // @Test(expected = IngredientAlreadyExistsException.class)
    // public void testAddIngredientsFailsWhenExistingIngredientIsPassed() throws ValueExistsInListException,
    //         IngredientAlreadyExistsException, FailedToAddIngredientException {
    //     Ingredient ing = mock(Ingredient.class);
    //
    //     doThrow(ValueExistsInListException.class).when(ingredientsDAO).addIngredientToAvailableIngredients(ing);
    //     ingredientsService.addIngredient(ing);
    // }
    //
    // @Test(expected = FailedToAddIngredientException.class)
    // public void testAddIngredientsFailsWhenIngredientAdditionFails() throws ValueExistsInListException,
    //         IngredientAlreadyExistsException, FailedToAddIngredientException {
    //     Ingredient ing = mock(Ingredient.class);
    //
    //     doThrow(RuntimeException.class).when(ingredientsDAO).addIngredientToAvailableIngredients(ing);
    //     ingredientsService.addIngredient(ing);
    // }
    //
    // @Test
    // public void testGetIngredientByIdReturnsObjectWhenPassedExistingIngredient() {
    //     List<Ingredient> ings = new ArrayList<>();
    //
    //     for (int i = 0; i < 5; i++) {
    //         Ingredient ing = mock(Ingredient.class);
    //         when(ing.getId()).thenReturn(new Long(i));
    //         ings.add(ing);
    //     }
    //
    //     when(ingredientsDAO.getAvailableIngredients()).thenReturn(ings);
    //
    //     assertEquals(ingredientsService.getIngredientById(1L).get(), ings.get(1));
    // }
    //
    // @Test
    // public void testGetIngredientByIdReturnsNoObjectWhenNonExistentIngredientIdIsPassed() {
    //     List<Ingredient> ings = new ArrayList<>();
    //
    //     for (int i = 0; i < 5; i++) {
    //         Ingredient ing = mock(Ingredient.class);
    //         when(ing.getId()).thenReturn(new Long(i));
    //         ings.add(ing);
    //     }
    //
    //     when(ingredientsDAO.getAvailableIngredients()).thenReturn(ings);
    //
    //     assertFalse(ingredientsService.getIngredientById(8L).isPresent());
    // }
    //
    // @Test
    // public void testGetIngredientsByTypeReturnsEmptyListWhenNoIngredientsWithTypeExist() {
    //     List<Ingredient> ings = new ArrayList<>();
    //
    //     for (int i = 0; i < 5; i++) {
    //         Ingredient ing = mock(Ingredient.class);
    //         when(ing.getType()).thenReturn(Ingredient.Type.MEAT);
    //         ings.add(ing);
    //     }
    //
    //     when(ingredientsDAO.getAvailableIngredients()).thenReturn(ings);
    //
    //     assert ingredientsService.getIngredientsByType(Ingredient.Type.ADDON).equals(Collections.emptyList());
    // }
    //
    // @Test
    // public void testGetIngredientsByTypeReturnsAListOfCorrectIngredients() {
    //     List<Ingredient> ings = new ArrayList<>();
    //
    //     for (int i = 0; i < 5; i++) {
    //         Ingredient ing = mock(Ingredient.class);
    //         when(ing.getType()).thenReturn(Ingredient.Type.MEAT);
    //         ings.add(ing);
    //     }
    //
    //     when(ingredientsDAO.getAvailableIngredients()).thenReturn(ings);
    //
    //     assert ingredientsService.getIngredientsByType(Ingredient.Type.MEAT).size() == 5;
    // }
}

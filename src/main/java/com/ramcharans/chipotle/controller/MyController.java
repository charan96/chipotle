package com.ramcharans.chipotle.controller;

//@RestController
//public class MyController {
//    @Autowired
//    IngredientsService ingredientsService;
//
//    @Autowired
//    OrderService orderService;
//
//    @GetMapping(value = "/availableIngredients", produces = "application/json")
//    public List<Ingredient> getAvailableIngredients() {
//        return ingredientsService.getAvailableIngredients();
//    }
//
//    @PostMapping(value = "/ingredient", consumes = "application/json", produces = "application/json")
//    @ResponseStatus(HttpStatus.OK)
//    public void addIngredient(@RequestBody Ingredient ingredient) {
//        ingredientsService.addIngredient(ingredient);
//
//        System.out.println(ingredientsService.getAvailableIngredients());
//    }
//
//    @PostMapping(value = "/greeting", consumes = "application/json", produces = "application/json")
//    public String createGreeting(@RequestBody Greeting greeting, @RequestParam String name) {
//        return greeting.createGreeting(name);
//    }
//
////    @PostMapping("/order")
////    public Order makeOrder(@RequestParam )
//
//
//}

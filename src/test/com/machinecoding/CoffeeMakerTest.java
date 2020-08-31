package test.com.machinecoding;

import com.machinecoding.CoffeeMaker;
import com.machinecoding.entity.Beverage;
import com.machinecoding.entity.Content;
import com.machinecoding.entity.Ingredient;
import com.machinecoding.exceptions.BeverageNotFoundException;
import com.machinecoding.exceptions.BeverageNotPreparedException;
import com.machinecoding.exceptions.IngredientNotFoundException;
import com.machinecoding.exceptions.InsufficientIngredientFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoffeeMakerTest {

    private CoffeeMaker coffeeMaker;

        /**
            While initialising we have kept the number of outlets to be 3
            while calling the coffeeMaker orderDrink method, we can create a thread pool of max size 3, and only those
            should be allowed to access that method, thus limiting how many drinks can be served parallely.
         **/
        @BeforeEach
        void initialiseCoffeeMaker() {
            Ingredient hotWater = new Ingredient("hot_water");
            Ingredient hotMilk = new Ingredient("hot_milk");
            Ingredient gingerSyrup = new Ingredient("ginger_syrup");
            Ingredient sugarSyrup = new Ingredient("sugar_syrup");
            Ingredient teaLeavesSyrup = new Ingredient("tea_leaves_syrup");
            Ingredient greenMixture = new Ingredient("green_mixture");

            Beverage hotTea = new Beverage("hot_tea", Arrays.asList(
                    new Content(hotWater, 200),
                    new Content(hotMilk, 100),
                    new Content(gingerSyrup, 10),
                    new Content(sugarSyrup, 10),
                    new Content(teaLeavesSyrup, 10)
            ));

            Beverage hotCoffee = new Beverage("hot_coffee", Arrays.asList(
                    new Content(hotWater, 100),
                    new Content(gingerSyrup, 30),
                    new Content(hotMilk, 400),
                    new Content(sugarSyrup, 50),
                    new Content(teaLeavesSyrup, 30)
            ));

            Beverage blackTea = new Beverage("black_tea", Arrays.asList(
                    new Content(hotWater, 300),
                    new Content(gingerSyrup, 30),
                    new Content(sugarSyrup, 50),
                    new Content(teaLeavesSyrup, 30)
            ));

            Beverage greenTea = new Beverage("green_tea", Arrays.asList(
                    new Content(hotWater, 100),
                    new Content(gingerSyrup, 30),
                    new Content(greenMixture, 30)
            ));

            this.coffeeMaker = new CoffeeMaker(3, Arrays.asList(
                    new Content(hotWater, 500),
                    new Content(hotMilk, 500),
                    new Content(gingerSyrup, 100),
                    new Content(sugarSyrup, 100),
                    new Content(teaLeavesSyrup, 100)
            ), Arrays.asList(hotTea, hotCoffee, blackTea, greenTea));
        }

        @Test
        public void orderDrink_throws_drinkNotInMenu() {
            BeverageNotFoundException e = Assertions.assertThrows(BeverageNotFoundException.class, () -> {
                coffeeMaker.orderDrink("cold_coffee");
            });
            assertEquals("BNF-001", e.getCode());
            assertEquals("cold_coffee cannot be prepared because it is not in menu", e.getErrorMessage());
        }

        @Test
        public void orderDrink() throws BeverageNotFoundException, BeverageNotPreparedException {
            String message = coffeeMaker.orderDrink("hot_coffee");
            assertEquals("hot_coffee is prepared",message);
        }

        @Test
        public void orderDrink_inParallel() {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        String result = coffeeMaker.orderDrink("hot_coffee");
                        assertEquals("hot_coffee is prepared", result);
                    } catch (Exception ex) {
                        ex.getMessage();
                    }
                }
            }).start();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        String result = coffeeMaker.orderDrink("hot_tea");
                        assertEquals("hot_tea is prepared", result);
                    } catch (Exception ex) {
                        ex.getMessage();
                    }
                }
            }).start();

            new Thread(new Runnable() {
                public void run() {
                    IngredientNotFoundException e = Assertions.assertThrows(IngredientNotFoundException.class, () -> {
                        coffeeMaker.orderDrink("green_tea");
                    });
                    assertEquals("INF-001", e.getCode());
                    assertEquals("green_tea cannot be prepared because green_mixture is not available", e.getErrorMessage());
                }
            }).start();

            new Thread(new Runnable() {
                public void run() {
                    InsufficientIngredientFoundException e = Assertions.assertThrows(InsufficientIngredientFoundException.class, () -> {
                        coffeeMaker.orderDrink("black_tea");
                    });
                    assertEquals("IIF-001", e.getCode());
                    assertEquals("black_tea cannot be prepared because hot_water is not sufficient", e.getErrorMessage());
                }
            }).start();
        }

    @Test
    public void orderDrink_ingredient_not_found() {

        new Thread(new Runnable() {
            public void run() {
                try {
                    String result = coffeeMaker.orderDrink("black_tea");
                    assertEquals("black_tea is prepared", result);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                IngredientNotFoundException e = Assertions.assertThrows(IngredientNotFoundException.class, () -> {
                    coffeeMaker.orderDrink("green_tea");
                });
                assertEquals("INF-001", e.getCode());
                assertEquals("green_tea cannot be prepared because green_mixture is not available", e.getErrorMessage());
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    String result = coffeeMaker.orderDrink("hot_coffee");
                    assertEquals("hot_coffee is prepared", result);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                InsufficientIngredientFoundException e = Assertions.assertThrows(InsufficientIngredientFoundException.class, () -> {
                    coffeeMaker.orderDrink("hot_tea");
                });
                assertEquals("IIF-001", e.getCode());
                assertEquals("hot_tea cannot be prepared because hot_water is not sufficient", e.getErrorMessage());
            }
        }).start();
    }

    @Test
    public void orderDrink_refill_valid_ingredient() {

        new Thread(new Runnable() {
            public void run() {
                try {
                    String result = coffeeMaker.orderDrink("black_tea");
                    assertEquals("black_tea is prepared", result);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                IngredientNotFoundException e = Assertions.assertThrows(IngredientNotFoundException.class, () -> {
                    coffeeMaker.orderDrink("green_tea");
                });
                assertEquals("INF-001", e.getCode());
                assertEquals("green_tea cannot be prepared because green_mixture is not available", e.getErrorMessage());
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    String result = coffeeMaker.orderDrink("hot_coffee");
                    assertEquals("hot_coffee is prepared", result);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    coffeeMaker.refillIngredient(new Ingredient("hot_water"),100);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    String result = coffeeMaker.orderDrink("hot_tea");
                    assertEquals("hot_tea is prepared", result);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        }).start();
    }

    @Test
    public void orderDrink_refill_invalid_ingredient() {

        new Thread(new Runnable() {
            public void run() {
                try {
                    String result = coffeeMaker.orderDrink("black_tea");
                    assertEquals("black_tea is prepared", result);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                IngredientNotFoundException e = Assertions.assertThrows(IngredientNotFoundException.class, () -> {
                    coffeeMaker.orderDrink("green_tea");
                });
                assertEquals("INF-001", e.getCode());
                assertEquals("green_tea cannot be prepared because green_mixture is not available", e.getErrorMessage());
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    String result = coffeeMaker.orderDrink("hot_coffee");
                    assertEquals("hot_coffee is prepared", result);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                IngredientNotFoundException e = Assertions.assertThrows(IngredientNotFoundException.class, () -> {
                    coffeeMaker.refillIngredient(new Ingredient("water"), 100);
                });
                assertEquals("INF-002", e.getCode());
                assertEquals("water ingredient is not available", e.getErrorMessage());
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    String result = coffeeMaker.orderDrink("hot_tea");
                    assertEquals("hot_tea is prepared", result);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        }).start();
    }

}

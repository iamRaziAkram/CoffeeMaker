package com.machinecoding;

import com.machinecoding.entity.Beverage;
import com.machinecoding.entity.Content;
import com.machinecoding.entity.Ingredient;
import com.machinecoding.exceptions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoffeeMaker {

    // Its value cannot be changed once initialised
    private final int availableSlots;

    // It will showcase the content present in the inventory at any point in time, which can also be refilled upon request
    private Map<Ingredient, Content> storage;

    // It will showcase the list of available beverages on menu, which cannot be changed
    private final Map<String, Beverage> beverages;

    /**
     * A public constructor to initialise the value of coffee maker
     * @param availableSlots
     * @param contents
     * @param beverages
     */
    public CoffeeMaker(int availableSlots, List<Content> contents, List<Beverage> beverages) {
        this.availableSlots = availableSlots;
        this.storage = new HashMap<>();
        this.beverages = new HashMap<>();

        for (Content content : contents) {
            this.storage.put(content.getIngredient(),content);
        }

        for(Beverage beverage: beverages) {
            this.beverages.put(beverage.getName().toLowerCase(), beverage);
        }
    }

    /**
     * This method can be used to refill any ingredient by any mentioned quantity
     * @param ingredient
     * @param quantity
     * @throws IngredientNotFoundException
     */
    public void refillIngredient(Ingredient ingredient, int quantity) throws IngredientNotFoundException {
        if (!this.storage.containsKey(ingredient)) {
            throw new IngredientNotFoundException("INF-002", ingredient.getName() + " ingredient is not available");
        } else {
            this.storage.get(ingredient).setQuantity(this.storage.get(ingredient).getQuantity() + quantity);
        }
    }

    /**
     * This method can be used to prepare beverages based on the name of beverage provided
     * @param beverageName
     * @return a success message string
     * @throws BeverageNotPreparedException
     * @throws BeverageNotFoundException
     */
    public String orderDrink(String beverageName) throws BeverageNotFoundException, BeverageNotPreparedException {
        Beverage beverage = this.beverages.get(beverageName);
        if(beverage != null) {
            this.prepareBeverage(beverage);
        } else {
            throw new BeverageNotFoundException("BNF-001", beverageName + " cannot be prepared because "
                    + "it is not in menu");
        }

        return beverage.getName() + " is prepared";
    }

    /**
     * This method is used to prepare individual beverage depending on the availability of ingredients in storage
     * @param beverage
     * @throws InsufficientIngredientFoundException
     * @throws IngredientNotFoundException
     */
    private synchronized void prepareBeverage(Beverage beverage) throws InsufficientIngredientFoundException, IngredientNotFoundException {
        List<Content> requiredContents = beverage.getContents();

        for (Content requiredContent : requiredContents) {
            // Check if the required ingredient is present
            if (!this.storage.containsKey(requiredContent.getIngredient())) {
                throw new IngredientNotFoundException("INF-001", beverage.getName() + " cannot be prepared because "
                        + requiredContent.getIngredient().getName() + " is not available");
            }

            // Check if the required ingredients is present in sufficient quantity
            Content availableIngredient = this.storage.get(requiredContent.getIngredient());
            if(availableIngredient.getQuantity() < requiredContent.getQuantity()) {
                throw new InsufficientIngredientFoundException("IIF-001", beverage.getName()
                        + " cannot be prepared because " + requiredContent.getIngredient().getName()
                        + " is not sufficient");
            }

            // Prepare the beverage and consume the ingredients
            availableIngredient.setQuantity(availableIngredient.getQuantity() - requiredContent.getQuantity());
        }
    }
}

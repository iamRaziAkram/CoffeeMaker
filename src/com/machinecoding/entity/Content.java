package com.machinecoding.entity;

// This entity represents the ingredient with their quantity
public class Content {

    // This attribute represents the ingredient
    private Ingredient ingredient;

    // This attribute represents the quantity of the ingredient
    private int quantity;

    public Content(Ingredient ingredient, int quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

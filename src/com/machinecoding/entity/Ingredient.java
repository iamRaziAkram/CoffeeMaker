package com.machinecoding.entity;

// This entity represents an ingredient
public class Ingredient {

    // This attribute represents the name of the ingredient
    private final String name;

    public Ingredient(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Ingredient)) return false;
        Ingredient that = (Ingredient) o;
        return this.name.equals(that.name);
    }
}

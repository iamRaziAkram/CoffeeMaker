package com.machinecoding.entity;

import java.util.ArrayList;
import java.util.List;

// This entity represents the individual beverage
public class Beverage {

    // This attribute represents the name of the beverage
    private String name;

    // This attribute represents the recipe of the beverage
    private List<Content> contents = new ArrayList<>();

    public Beverage(String name, List<Content> contents) {
        this.name = name;
        this.contents = contents;
    }

    public String getName() {
        return this.name;
    }

    public List<Content> getContents() {
        return contents;
    }
}

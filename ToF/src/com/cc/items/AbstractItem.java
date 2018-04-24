/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.items;

/**
 * An abstract Item.
 * @author Ivan Canet
 */
public abstract class AbstractItem implements Item {

    protected int weight;
    protected String description;
    protected final Rarity rarity;
    protected String name;

    /**
     * Creates an Abstract Item.
     * @param name the name of the Item
     * @param weight the weight of the object in grams
     * @param description the description of the item
     * @param rarity the rarity of the item
     */
    public AbstractItem(String name, int weight, String description, Rarity rarity) {
        if(description == null)
            throw new IllegalArgumentException("The description of an item cannot be null.");
        if(name == null)
            throw new IllegalArgumentException("The description of an item cannot be null.");
        if(weight <= 0)
            throw new IllegalArgumentException("Weight cannot be negative or null.");
        
        this.name = name;
        this.weight = weight;
        this.description = description;
        this.rarity = rarity;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Rarity getRarity() {
        return rarity;
    }
    
}

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

    /**
     * Creates an Abstract Item.
     * @param weight the weight of the object in grams
     * @param description the description of the item
     * @param rarity the rarity of the item
     */
    public AbstractItem(int weight, String description, Rarity rarity) {
        this.weight = weight;
        this.description = description;
        this.rarity = rarity;
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

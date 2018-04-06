/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.items;

/**
 * Represents a generic Item.
 * @author Ivan Canet
 */
public interface Item {
    
    /**
     * The weight of this Item, in grams.
     * <p>Note that the weight of an Item is a constant, and can NOT change.
     * @return The weight of this Item.
     */
    public int getWeight();
    
    /**
     * The name of this Item.
     * @return The name of this Item.
     */
    public String getName();
    
    /**
     * The description of this Item.
     * @return The description of this Item.
     */
    public String getDescription();
    
    /**
     * The rarity of an Item.
     * @return The rarity of an Item.
     */
    public Rarity getRarity();
    
}

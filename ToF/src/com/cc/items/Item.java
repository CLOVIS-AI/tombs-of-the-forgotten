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
     * @return The weight of this Item.
     */
    public int getWeight();
    
    /**
     * The description of this Item.
     * @return The description of this Item.
     */
    public String getDescription();
    
}

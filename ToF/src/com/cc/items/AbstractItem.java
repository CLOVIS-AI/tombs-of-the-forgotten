/* MIT License
 *
 * Copyright (c) 2018 Canet Ivan & Chourouq Sarah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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

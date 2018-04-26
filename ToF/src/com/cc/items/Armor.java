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

import com.cc.players.Entity;

/**
 * Armor are objects that protect entities.
 * @author Ivan Canet
 */
public class Armor extends AbstractItem {
    
    protected int damage;
    
    /**
     * Creates an armor object.
     * @param name the name of the item
     * @param weight the weight of the item
     * @param description the description of the item
     * @param rarity the rarity of the item
     * @param damage the damage reduction of the item
     */
    public Armor(String name, int weight, String description, Rarity rarity, 
            int damage) {
        super(name, weight, description, rarity);
        
        if(damage <= 0)
            throw new IllegalArgumentException("Damage cannot be negative or "
                    + "null: " + damage);
        
        this.damage = damage;
    }

    @Override
    public void use(Entity entity) {
        // Attacking someone with your armor results in 1 damage point
        entity.getOpponent()
                .ifPresent(p -> p.hurt(1));
    }
    
    /**
     * Gets the damage reduction provided by this item.
     * @return the damage reduction
     */
    public int getDamageReduction(){
        return damage;
    }
    
}

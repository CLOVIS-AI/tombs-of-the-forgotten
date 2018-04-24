/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

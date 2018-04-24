/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.items;

import com.cc.players.Entity;
import java.util.function.Consumer;

/**
 * A Magical Item.
 * <p>Magical Items can only be used by consuming a certain amount of mana.
 * @author Ivan Canet
 */
public class MagicalItem extends AbstractItem {

    protected int manaCost;
    protected Consumer<Entity> action;
    
    /**
     * Constructs a Magical Item using a lambda-expression as an effect.
     * @param name the name of the item
     * @param weight the weight of the item
     * @param description the description of the item
     * @param rarity the rarity of the item
     * @param manaCost the mana cost of the item
     * @param action what happens when you use it
     * @see #MagicalItem(int, java.lang.String, com.cc.items.Rarity, int, int) Magical Weapon
     */
    public MagicalItem(String name, int weight, String description, Rarity rarity, 
            int manaCost, Consumer<Entity> action) {
        super(name, weight, description, rarity);
        this.manaCost = manaCost;
        this.action = action;
    }
    
    /**
     * Constructs a Magical weapon.
     * @param name the name of the weapon
     * @param weight the weight of the weapon
     * @param description the description of the weapon
     * @param rarity the rarity of the weapon
     * @param manaCost the mana cost of using the weapon
     * @param damage the damage dealt by the weapon (see {@link Entity#hurt(int) })
     * @see #MagicalItem(int, java.lang.String, com.cc.items.Rarity, int, java.util.function.Consumer) Magical Item
     */
    public MagicalItem(String name, int weight, String description, Rarity rarity, 
            int manaCost, int damage) {
        this(name, weight, description, rarity, manaCost, 
                e -> e.getOpponent().ifPresent(f -> f.hurt(damage)));
    }

    @Override
    public void use(Entity entity) {
        if(entity.getMana() < manaCost)
            return;
        
        action.accept(entity);
        entity.useMana(manaCost);
    }
    
}

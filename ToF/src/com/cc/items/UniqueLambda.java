/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.items;

import com.cc.players.Entity;
import java.util.function.Consumer;

/**
 * UniqueUsage items cannot be used twice; as soon as they are used they will
 * reject any further call to their {@link #use(com.cc.players.Entity) } method.
 * <p>Note for the devs: when implementing an item than can only be used once
 * that has a complicated action, look at {@link UniqueUsage}.
 * @author Ivan Canet
 */
public class UniqueLambda extends UniqueUsage {

    protected Consumer<Entity> action;
    
    /**
     * Constructs a single-use item with a lambda-expression.
     * @param weight its weight
     * @param description its description
     * @param rarity its rarity
     * @param action what happens when an entity uses the item
     */
    public UniqueLambda(int weight, String description, Rarity rarity, Consumer<Entity> action) {
        super(weight, description, rarity);
        this.action = action;
    }

    @Override
    protected void act(Entity entity) {
        action.accept(entity);
    }
    
}

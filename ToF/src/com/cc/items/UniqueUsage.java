/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.items;

import com.cc.players.Entity;

/**
 * UniqueUsage items cannot be used twice; as soon as they are used they will
 * reject any further call to their {@link #use(com.cc.players.Entity) } method.
 * <p>Note for the devs: when implementing an item than can only be used once
 * that has a very simple action, look at {@link UniqueLambda}.
 * @author Ivan Canet
 */
public abstract class UniqueUsage extends AbstractItem {
    
    private boolean hasBeenUsed = false;

    /**
     * Creates an item that can only be used once.
     * @param name its name
     * @param weight its weight
     * @param description its description
     * @param rarity its rarity
     */
    public UniqueUsage(String name, int weight, String description, Rarity rarity) {
        super(name, weight, description, rarity);
    }

    /**
     * Uses this item.
     * @param entity the entity you want to use this item on
     * @throws AlreadyUsedException if this item has already been used
     */
    @Override
    public final void use(Entity entity) throws AlreadyUsedException {
        if(hasBeenUsed)
            throw new AlreadyUsedException(this);
        
        trigger(entity);
    }
    
    /**
     * Tries to use this item. If it is already used, the call is aborted.
     * @param entity the entity you want to use this item on
     */
    public final void tryUse(Entity entity) {
        if(!hasBeenUsed)
            trigger(entity);
    }
    
    private void trigger(Entity entity) {
        act(entity);
        hasBeenUsed = true;
    }
    
    /**
     * Has this item already been used?
     * @return {@code true} if it already has been.
     */
    public final boolean hasBeenUsed(){
        return hasBeenUsed;
    }
    
    /**
     * The actual action of this item.
     * <p>Override this method to create a custom single-use item.
     * @param entity the entity the item is used on
     */
    protected abstract void act(Entity entity);
    
    /**
     * This exception is thrown when the {@link #use(com.cc.players.Entity) } 
     * method is called on an item that has already been used.
     */
    public static class AlreadyUsedException extends IllegalStateException {
        private AlreadyUsedException(Item i){
            super("The item " + i.toString() + " has already been used.");
        }
    }
}

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
import com.eclipsesource.json.JsonObject;

/**
 * UniqueUsage items cannot be used twice; as soon as they are used they will
 * reject any further call to their {@link #use(com.cc.players.Entity) } method.
 * <p>Note for the devs: when implementing an item than can only be used once
 * that has a very simple action, look at {@link UniqueLambda}. This class is
 * written as package-private because the serialization is not able to load 
 * direct children.
 * @author Ivan Canet
 */
abstract class UniqueUsage extends AbstractItem {
    
    private boolean hasBeenUsed = false;
    
    /**
     * Creates a UniqueUsage item from JSON
     * @param json the saved data
     */
    public UniqueUsage(JsonObject json){
        super(json);
        hasBeenUsed = json.getBoolean("used", false);
    }

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
    
    @Override
    public JsonObject save(){
        return super.save()
                .add("used", hasBeenUsed);
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

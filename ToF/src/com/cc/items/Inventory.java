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

import com.cc.utils.Bar;
import static com.cc.utils.Bar.Behavior.ACCEPT;
import com.eclipsesource.json.JsonObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author schourouq
 */
public class Inventory extends ItemContainer {
    
    /** Weigth bar. Can't add item into the inventory when full.*/
    private final Bar weight;
    
    /**
     * Loads the inventory from JSON
     * @param json the saved data
     */
    public Inventory(JsonObject json) {
        super(json);
        weight = new Bar(json.get("weight").asObject());
    }
    
    public Inventory(String description, int maxWeight) {
        super(description);
        weight = new Bar("Pods", 0, maxWeight, 0);
    }
    
     /**
     * Verifies that one player can add a specific item to the inventory (based
     * on the weight one can carry).
     * @param item The item
     * @return Whether one player is strong enough to take this item.
     */
    public boolean canAdd(Item item) {
        return weight.getCurrent() + item.getWeight() < weight.getMaximum();
    }
    
    
     /**
     * Adds an item to one's inventory.
     * <p>If the item is too heavy for the player, nothing is done. See 
     * {@link #canAdd(com.cc.items.Item) canAddItem(Item)}.
     * @param item The item to be added.
     */
    @Override
    public void add(Item item) {
        if (canAdd(item)) {
            super.add(item);
            weight.add(item.getWeight(), ACCEPT);
        }
    }
    
    /**
     * Adds as many items as possible from a Collection of items.
     * <p>An item can be added if its weight is not too much for a player to
     * carry. {@link #canAdd(com.cc.items.Item) canAddItem(Item)}.
     * @param items a Collection of items.
     * @return The items that couldn't be added. If none, an empty list is returned.
     */
    public List<Item> addIfPossible(Collection<Item> items){
        List<Item> couldnt = new ArrayList<>();
        
        for(Item i : items){
            if(canAdd(i))
                super.add(i);
            else
                couldnt.add(i);
        }
        
        return couldnt;
    }
    
    /**
     * Gets the weight amount of the player's inventory.
     * @return The weight of the inventory.
     */
    public int getWeight() {
        return weight.getCurrent();
    }
    
    /**
     * Gets the weight bar.
     * @return The weight bar.
     */
    public Bar getWeightBar() {
        return weight;
    }
    
    @Override
    public JsonObject save() {
        return super.save()
                .add("weight", weight.save());
    }
    
}

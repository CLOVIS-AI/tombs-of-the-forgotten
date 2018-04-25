/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.items;

import com.cc.utils.Bar;
import static com.cc.utils.Bar.Behavior.ACCEPT;
import static com.cc.utils.Translator.LINES;
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
    
    public Inventory(String description, int maxWeight) {
        super(description);
        weight = new Bar(LINES.get("pods"), 0, maxWeight, 0);
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
    
}

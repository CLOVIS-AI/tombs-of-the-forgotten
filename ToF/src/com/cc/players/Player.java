/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.players;

import com.cc.items.Item;
import com.cc.utils.Bar;
import static com.cc.utils.Bar.Behavior.ACCEPT;
import static com.cc.utils.Translator.LINES;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A Player.
 * @author Ivan Canet
 */
public class Player extends Entity {
    
    private final Bar weight;
    private final List<Item> items;
    
    private int searchLuck;
    
    /**
     * Creates a default player with default values.
     */
    public Player(){
        this(20, 2, 5, 10);
    }
    
    /**
     * Creates a player.
     * @param maxHealth The maximum health
     * @param maxStrength The maximum strength
     * @param maxMana The maximum mana
     * @param maxWeight The maximum weight he can carry
     */
    public Player(int maxHealth, int maxStrength, int maxMana, int maxWeight) {
        super(maxHealth, maxStrength, maxMana);
        searchLuck = 0;
        items = new ArrayList<>();
        
        weight = new Bar(LINES.get("weight"), 0, maxWeight, 0);
    }
    
    /**
     * Verifies that this player can add a specific item to their inventory (based
     * on the weight they can carry).
     * @param item The item
     * @return Whether this player is strong enough to take this item.
     */
    public boolean canAddItem(Item item){
        return weight.getCurrent() + item.getWeight() < weight.getMaximum();
    }
    
    /**
     * Adds an item to this player's inventory.
     * <p>If the item is too heavy for the player, nothing is done. See 
     * {@link #canAddItem(com.cc.items.Item) canAddItem(Item)}.
     * @param item The item to be added.
     */
    public void addItem(Item item){
        if(canAddItem(item)){
            items.add(item);
            weight.add(item.getWeight(), ACCEPT);
        }
    }
    
    /**
     * Adds as many items as possible from a Collection of items.
     * <p>An item can be added if its weight is not too much for a player to
     * carry. {@link #canAddItem(com.cc.items.Item) canAddItem(Item)}.
     * @param items a Collection of items.
     * @return The items that couldn't be added. If none, an empty list is returned.
     */
    public List<Item> addItemsIfPossible(Collection<Item> items){
        List<Item> couldnt = new ArrayList<>();
        
        for(Item i : items){
            if(canAddItem(i))
                addItem(i);
            else
                couldnt.add(i);
        }
        
        return couldnt;
    }
    
    /**
     * Removes an item from the inventory of a player.
     * <p>If the player doesn't have this item, nothing happens.
     * @param item The item to be taken from the player's inventory
     */
    public void removeItem(Item item){
        if(items.contains(item)){
            items.remove(item);
            weight.remove(item.getWeight(), ACCEPT);
        }
    }
    
    @Override
    public List<Bar> getBars(){
        List<Bar> bars = super.getBars();
        bars.add(weight);
        return bars;
    }
    
}

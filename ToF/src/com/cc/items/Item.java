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
import com.cc.players.Entity.Stat;
import com.cc.utils.Bar;
import static com.cc.utils.Bar.Behavior.ACCEPT;
import com.cc.utils.Save;
import com.cc.utils.messages.Message;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Represents an Item.
 * @author Ivan Canet
 */
public final class Item implements Save<JsonObject> {
    
    private final List<Action> actions;
    private final String name;
    private final String description;
    private final Rarity rarity;
    private final int weight;
    private final Bar durability;
    private int id;
    
    /**
     * Creates a new Item.
     * @param name its name
     * @param description its description
     * @param rarity its rarity
     * @param weight its weight (in grams)
     * @param durability its durability
     */
    public Item(String name, String description, Rarity rarity, int weight,
            Bar durability){
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.weight = weight;
        this.durability = new Bar(durability);
        actions = new ArrayList<>();
        id = 0;
    }
    
    /**
     * Creates an Item.
     * @param name its name
     * @param description its description
     * @param rarity its rarity
     * @param weight its weight (in grams)
     * @param maxDurability its maximum durability
     */
    public Item(String name, String description, Rarity rarity, int weight,
            int maxDurability){
        this(name, description, rarity, weight,
             new Bar("Durability", 0, maxDurability, maxDurability));
    }
    
    /**
     * Creates an Item.
     * @param name its name
     * @param description its description
     * @param rarity its rarity
     * @param weight its weight (in grams)
     * @param maxDurability its maximum durability
     * @param actions its actions
     */
    public Item(String name, String description, Rarity rarity, int weight,
            int maxDurability, List<Action> actions){
        this(name, description, rarity, weight,
             new Bar("Durability", 0, maxDurability, maxDurability));
        this.actions.addAll(actions);
    }
    
    /**
     * Loads an Item from JSON data.
     * @param json the saved data
     */
    public Item(JsonObject json){
        this(               json.getString("name", null),
                            json.getString("description", null),
             Rarity.valueOf(json.getString("rarity", null)),
                            json.getInt("weight", 0),
                    new Bar(json.get("durability").asObject()));
        
        id = json.getInt("id", 0);
        
        JsonArray acts = json.get("actions").asArray();
        for(JsonValue j : acts){
            actions.add(Action.load(j.asObject()));
        }
    }
    
    /**
     * Sets the unique identifier of this item.
     * @param id The identifier.
     */
    public void setId(int id){
        this.id = id;
    }
    
    /**
     * Gets the unique identifier of this item.
     * <p>An ID is not always needed for items. When there is no ID, it defaults
     * to 0. IDs are used (for example) by LockedDoors.
     * @return The identifier
     */
    public int getId(){
        return id;
    }
    
    /**
     * Uses this item.
     * @param entity the entity that uses the Item
     */
    void use(Entity entity){
        if(isBroken())
            return;
        
        actions.forEach(a -> a.execute(entity));
        durability.remove(1, ACCEPT);
    }
    
    /**
     * Can an entity use this item?
     * @param entity the entity
     * @return {@code true} if it can.
     */
    public boolean canUse(Entity entity){
        if(isBroken())
            return false;
        
        return actions.stream().allMatch(a -> a.canUse(entity));
    }
    
    /**
     * Gets the effects of this Item.
     * @return the effects of this Item.
     */
    public List<Message> getEffects(){
        ArrayList<Message> messages = new ArrayList<>();
        actions.forEach(a -> messages.add(a.getEffects()));
        return messages;
    }
    
    /**
     * Returns the actions. Note: do not confuse with #getEffects.
     * @return The actions.
     */
    public Stream<Action> getActions(){
        return actions.stream();
    }
    
    /**
     * The weight of this Item, in grams.
     * <p>Note that the weight of an Item is a constant, and can NOT change.
     * @return The weight of this Item.
     */
    public int getWeight(){
        return weight;
    }
    
    /**
     * The name of this Item.
     * @return The name of this Item.
     */
    public String getName(){
        return name;
    }
    
    /**
     * The description of this Item.
     * @return The description of this Item.
     */
    public String getDescription(){
        return description;
    }
    
    /**
     * The rarity of an Item.
     * @return The rarity of an Item.
     */
    public Rarity getRarity(){
        return rarity;
    }
    
    /**
     * Is this item broken?
     * @return {@code true} if the item's durability is lesser or equal 0.
     */
    public boolean isBroken(){
        return durability.getCurrent() <= 0;
    }
    
    /**
     * The durability of this Item.
     * @return How many times can this item be used?
     */
    public int getDurability(){
        return durability.getCurrent();
    }
    
    /**
     * The maximum durability of this Item.
     * @return How many times can this item be used from its creation until it
     *         breaks.
     */
    public int getMaxDurability(){
        return durability.getMaximum();
    }
    
    /**
     * Gets the way a stat is modified by worn effects of this object.
     * @param stat the stat
     * @return The effect on the specified stat
     */
    public int getWear(Stat stat){
        return actions.stream()
                .mapToInt(a -> a.getWear(stat))
                .sum();
    }

    @Override
    public JsonObject save() {
        JsonArray acts = new JsonArray();
        actions.forEach(a -> acts.add(a.save()));
        
        return new JsonObject()
                .add("name", name)
                .add("description", description)
                .add("rarity", rarity.name())
                .add("weight", weight)
                .add("actions", acts)
                .add("durability", durability.save())
                .add("id", id);
    }
    
    @Override
    public String toString() {
        return name + ": " + getEffects();
    }
    
    /**
     * A class that allows to add actions to an item (because items are read-only
     * objects).
     */
    public static class ItemBuilder {
        
        private final Item item;
        
        /**
         * Creates a new Item.
         * @param name its name
         * @param description its description
         * @param rarity its rarity
         * @param weight its weight (in grams)
         * @param durability its durability
         */
        public ItemBuilder(String name, String description, Rarity rarity, int weight,
                Bar durability){
            item = new Item(name, description, rarity, weight, durability);
        }

        /**
         * Creates an Item.
         * @param name its name
         * @param description its description
         * @param rarity its rarity
         * @param weight its weight (in grams)
         * @param maxDurability its maximum durability
         */
        public ItemBuilder(String name, String description, Rarity rarity, int weight,
                int maxDurability){
            item = new Item(name, description, rarity, weight, maxDurability);
        }
        
        /**
         * Adds an action to the item that will be generated by this object.
         * @param a an action
         * @return This builder itself, to allow method-chaining.
         */
        public ItemBuilder add(Action a){
            if(a == null)
                throw new NullPointerException("The action cannot be null.");
            
            item.actions.add(a);
            return this;
        }
        
        /**
         * Returns the item built by this builder.
         * @return The item.
         */
        public Item get(){
            return item;
        }
        
    }
}

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
import com.cc.utils.Bar;
import static com.cc.utils.Bar.Behavior.ACCEPT;
import com.cc.utils.Save;
import com.cc.utils.messages.Message;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.util.ArrayList;
import java.util.List;

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
     * Loads an Item from JSON data.
     * @param json the saved data
     */
    public Item(JsonObject json){
        this(               json.getString("name", null),
                            json.getString("description", null),
             Rarity.valueOf(json.getString("rarity", null)),
                            json.getInt("weight", 0),
                    new Bar(json.get("durability").asObject()));
        
        JsonArray acts = json.get("actions").asArray();
        for(JsonValue j : acts){
            actions.add(Action.load(j.asObject()));
        }
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
     * Gets the effects of this Item.
     * @return the effects of this Item.
     */
    public List<Message> getEffects(){
        ArrayList<Message> messages = new ArrayList<>();
        actions.forEach(a -> a.addEffects(messages));
        return messages;
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

    @Override
    public JsonObject save() {
        JsonArray acts = new JsonArray();
        actions.forEach(a -> acts.add(a.save()));
        
        return new JsonObject()
                .add("name", name)
                .add("description", name)
                .add("rarity", rarity.name())
                .add("weight", weight)
                .add("actions", acts)
                .add("durability", durability.save());
    }
}

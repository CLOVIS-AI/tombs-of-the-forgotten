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
import com.cc.utils.Save;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic Item.
 * @author Ivan Canet
 */
public final class Item implements Save<JsonObject> {
    
    private final List<Action> actions;
    private final String name;
    private final String description;
    private final Rarity rarity;
    private final int weight;
    private final Bar durability;
    
    public Item(String name, String description, Rarity rarity, int weight,
            Bar durability){
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.weight = weight;
        this.durability = new Bar(durability);
        actions = new ArrayList<>();
    }
    
    public Item(String name, String description, Rarity rarity, int weight,
            int maxDurability){
        this(name, description, rarity, weight,
             new Bar("Durability", 0, maxDurability, maxDurability));
    }
    
    public Item(JsonObject json){
        this(json.getString("name", null),
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
     * Notifies the Item that an entity is using it.
     * @param entity the entity that uses the Item
     */
    public void use(Entity entity){
        actions.forEach(a -> a.execute(entity));
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

    @Override
    public JsonObject save() {
        JsonArray acts = new JsonArray();
        actions.forEach(a -> acts.add(a.save()));
        
        return new JsonObject()
                .add("name", name)
                .add("description", name)
                .add("rarity", rarity.name())
                .add("weight", weight)
                .add("actions", acts);
    }
}

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
import com.cc.utils.Save;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;

/**
 * Represents a generic Item.
 * @author Ivan Canet
 */
public interface Item extends Save<JsonObject> {
    
    /**
     * Notifies the Item that an entity is using it.
     * @param entity the entity that uses the Item
     */
    public void use(Entity entity);
    
    /**
     * The weight of this Item, in grams.
     * <p>Note that the weight of an Item is a constant, and can NOT change.
     * @return The weight of this Item.
     */
    public int getWeight();
    
    /**
     * The name of this Item.
     * @return The name of this Item.
     */
    public String getName();
    
    /**
     * The description of this Item.
     * @return The description of this Item.
     */
    public String getDescription();
    
    /**
     * The rarity of an Item.
     * @return The rarity of an Item.
     */
    public Rarity getRarity();
    
    public static Item loadItem(JsonObject json){
        if(json.contains("stamina-cost"))   return new Weapon(json);
        if(json.contains("damage"))         return new Armor(json);
        if(json.contains("mana-cost"))      return new MagicalItem(json);
        if(json.contains("used"))           return new UniqueLambda(json);
        
        throw new IllegalArgumentException("No Item type was found that matches"
                + "the JSON data: " + json.toString(WriterConfig.PRETTY_PRINT));
    }
}

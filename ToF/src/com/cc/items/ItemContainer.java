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

import com.cc.utils.Save;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents a container.
 * @author schourouq
 */
public class ItemContainer implements Save<JsonObject> {
        
    private final List<Item> items;
    
    private final Optional<String> description;
    
    /**
     * Loads an ItemContainer from JSON.
     * @param json the saved data
     */
    public ItemContainer(JsonObject json) {
        this(json.getString("description", null));
        json.get("items").asArray()
                .forEach(i -> items.add(new Item(i.asObject())));
    }
    
    /**
     * Creates a chest.
     * @param description Description of a chest.
     */
    public ItemContainer(Optional<String> description) {
        this.description = description;
        items = new ArrayList<>();
    }

    /**
     * Creates a chest
     * @param description Description of a chest
     */
    public ItemContainer(String description) {
        this((description == null) ? 
            Optional.empty() : 
            Optional.of(description));
    }

    /**
     * A COPY of the items.
     * @return A container.
     */
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * The description of a container.
     * @return The description of a container.
     */
    public Optional<String> getDescription() {
        return description;
    }

    /**
     * Remove an item from this object.
     * @param item An item.
     * @return {@code true} if this list contained the specified element.
     */
    public boolean remove(Item item) {
        return items.remove(item);
    }
    
    /**
     * Verify if a container contains a specific item.
     * @param item An item
     * @return <code> true </code>: A container contains a specific item.
     * <code> false </code> otherwise.
     */
    public boolean contains(Item item) {
        return items.contains(item);
    }
    
    /**
     * Selects the weapons in this inventory, in no particular order.
     * @return The weapons in this inventory.
     */
    public Stream<Weapon> selectWeapons() {
        return items.stream()
                .unordered()
                .filter(i -> i instanceof Weapon)
                .map(i -> (Weapon) i);
    }
    
    /**
     * Selects the armors in this inventory, in no particular order.
     * @return The armors in this inventory.
     */
    public Stream<Armor> selectArmors() {
        return items.stream()
                .unordered()
                .filter(i -> i instanceof Armor)
                .map(i -> (Armor) i);
    }
    
    /**
     * Selects the magical items in this inventory, in no particular order.
     * @return The magical items in this inventory.
     */
    public Stream<MagicalItem> selectMagicals() {
        return items.stream()
                .unordered()
                .filter(i -> i instanceof MagicalItem)
                .map(i -> (MagicalItem) i);
    }
    
    /**
     * Selects the unique usage items in this inventory, in no particular order.
     * @return The unique usage items in this inventory.
     */
    public Stream<UniqueUsage> selectUnique() {
        return items.stream()
                .unordered()
                .filter(i -> i instanceof UniqueUsage)
                .map(i -> (UniqueUsage) i);
    }
    
    /**
     * Add an item into this object.
     * @param item The item.
     */
    public void add(Item item) {
        items.add(item);
    }
    
    /**
     * A hash code value for the object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.items);
        hash = 67 * hash + Objects.hashCode(this.description);
        return hash;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument,
     * false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ItemContainer other = (ItemContainer) obj;
        if (!Objects.equals(this.items, other.items)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }

    @Override
    public JsonObject save() {
        JsonArray items = new JsonArray();
        this.items.forEach(e -> items.add(e.save()));
        
        return new JsonObject()
                .add("description", description.orElse(""))
                .add("items", items);
    }
    
}

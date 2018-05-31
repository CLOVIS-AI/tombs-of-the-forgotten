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

import static com.cc.items.EntityAction.Mode.PERMANENT;
import static com.cc.items.EntityAction.Operation.ADD;
import static com.cc.items.EntityAction.Operation.REMOVE;
import static com.cc.items.EntityAction.Target.OPPONENT;
import static com.cc.items.EntityAction.Target.SELF;
import com.cc.players.Entity.Stat;
import static com.cc.players.Entity.Stat.HEALTH;
import static com.cc.players.Entity.Stat.MANA;
import com.cc.utils.Bar;
import com.cc.utils.Save;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
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
        return Collections.unmodifiableList(items);
    }
    
    /**
     * Gets all the items that can only be used once.
     * <p>Note that items that can be used multiple times (eg. 5 times) but were
     * already almost used (eg. used 4 times, so 1 use is left) <b>are</b>
     * included.
     * @return The items that can only be used once.
     */
    public Stream<Item> getUniqueUsage() {
        return items.stream()
                .filter(i -> i.getDurability() == 1);
    }
    
    /**
     * Gets all the weapons.
     * <p>The weapons are the items that deal damage to the opponent - that is,
     * items that have at least one action similar to OPPONENT HEALTH REMOVE...
     * @return The weapons.
     */
    public Stream<Item> getWeapons() {
        return items.stream()
                .filter(i -> i.getActions()
                                .map(a -> (EntityAction) a)
                                .anyMatch(a -> a.getTarget() == OPPONENT
                                            && a.getStat() == HEALTH
                                            && a.getOperation() == REMOVE));
    }
    
    /**
     * Gets all the wearables.
     * <p>The wearables are items that can be worn - that is, items that have at
     * least one action similar to SELF PERMANENT...
     * @return The items you can wear.
     */
    public Stream<Item> getWearables() {
        return items.stream()
                .filter(i -> i.getActions()
                                .map(a -> (EntityAction) a)
                                .anyMatch(a -> a.getTarget() == SELF
                                            && a.getMode() == PERMANENT));
    }
    
    /**
     * Gets all the scrolls.
     * <p>The scrolls are the items that {@link #getUniqueUsage() can only be
     * used once} and cost mana.
     * @return The scrolls.
     */
    public Stream<Item> getScrolls() {
        return getUniqueUsage()
                .filter(i -> i.getActions()
                                .map(a -> (EntityAction) a)
                                .anyMatch(a -> a.getTarget() == SELF
                                            && a.getOperation() == REMOVE
                                            && a.getStat() == MANA));
    }
    
    /**
     * Gets all the edible items.
     * <p>The edible items are the items that {@link #getUniqueUsage() can only
     * be used once} and increase any of the user's stats.
     * @return The scrolls.
     */
    public Stream<Item> getEdible() {
        return getUniqueUsage()
                .filter(i -> i.getActions()
                                .map(a -> (EntityAction) a)
                                .anyMatch(a -> a.getOperation() == ADD
                                            && a.getTarget() == SELF));
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
     * Add an item into this object.
     * @param item The item.
     * @return {@code true} if the item was successfully added.
     */
    public boolean add(Item item) {
        items.add(item);
        return true;
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
    
    /**
     * Adds to the bar the total amount of every bonus of type "permanent".
     * @param stat the stat that is linked to the bar
     * @param bar the bar
     */
    public void applyWears(Stat stat, Bar bar){
        for(Item item : items){
            int wear = item.getWear(stat);
            if(wear != 0)
                bar.addBonus(1, wear, false);
        }
    }
    
    /**
     * Returns a spliterator of the items contained in this object.
     * @return A spliterator of the items in this object.
     * @see List#spliterator() 
     */
    public Spliterator<Item> spliterator(){
        return items.spliterator();
    }
    
    /**
     * Creates a stream of the items.
     * @return A stream of the items.
     * @see List#stream() 
     */
    public Stream<Item> stream(){
        return items.stream();
    }
    
    /**
     * Creates a parallel stream of the items.
     * @return A parallel stream of the items.
     * @see List#parallelStream() 
     */
    public Stream<Item> parallelStream(){
        return items.parallelStream();
    }
}

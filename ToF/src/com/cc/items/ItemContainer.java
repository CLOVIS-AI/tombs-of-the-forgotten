/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a container.
 * @author schourouq
 */
public class ItemContainer {
        
    private final List<Item> items;
    
    private final Optional<String> description;
    
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
    
}

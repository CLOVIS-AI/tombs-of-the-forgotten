/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.items;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Represents a container.
 * @author schourouq
 */
public class Chest {
        
    private Map<String, Item> container;
    
    private Optional<String> description;
    
    /**
     * Creates a chest.
     * @param description Description of a chest.
     */
    public Chest(Optional<String> description) {
        this.description = description;
        container = new TreeMap<>();
    }

    /**
     * Creates a chest
     * @param description Description of a chest
     */
    public Chest(String description) {
        this.description = (description == null) ? 
            Optional.empty() : 
            Optional.of(description);
    }

    /**
     * A container.
     * @return A container.
     */
    public Map<String, Item> getContainer() {
        return container;
    }

    /**
     * The description of a container.
     * @return The description of a container.
     */
    public Optional<String> getDescription() {
        return description;
    }

    /**
     * A hash code value for the object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.container);
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
        final Chest other = (Chest) obj;
        if (!Objects.equals(this.container, other.container)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }
    
    /**
     * Remove an item from a chest.
     * @param chest A chest.
     * @param item An item.
     */
    public void removeItem(Map<String, Item> chest, Item item) {
        if (contains(chest, item))
            chest.remove(item);
    }
    
    public void remove(Item item) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Verify if a container contains a specific item.
     * @param container A container.
     * @param item An item
     * @return <code> True </code>: A container contains a specific item.
     * <code> False </code> otherwise.
     */
    public boolean contains(Map<String, Item> container, Item item) {
        return container.containsValue(item);
    }
    
    public boolean contains(Item item) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Add an item into a container.
     * @param name The name of the item.
     * @param item The item.
     */
    public void addItem(String name, Item item) {
        container.put(name, item);
    }
    
    public void add(Item i) {
        throw new UnsupportedOperationException();
    }
    
}

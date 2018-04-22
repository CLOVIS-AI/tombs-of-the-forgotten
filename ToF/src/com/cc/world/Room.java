/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world;

import com.cc.world.links.Link;
import java.util.Map;
import java.util.Objects;

/**
 * An ingame room.
 * @author Ivan Canet
 */
public class Room {
    
    private Map<Direction, Link> neighbors;
    
    private String description;
    
    private Location location;
    private World world;
    
    private boolean isGenerated = false;
    
    /**
     * Sets the world of this object. This method can only be called once, during
     * the generation phase.
     * @param w the world of this object.
     * @return This room itself, to allow method-chaining.
     */
    public Room setWorld(World w){
        if(world != null && !isGenerated)
            throw new IllegalStateException("This method should only be called once.");
        
        world = w;
        return this;
    }
    
    /**
     * Sets the location of this object. This method can only be called once, during
     * the generation phase.
     * @param l the location of this object.
     * @return This room itself, to allow method-chaining.
     */
    public Room setLocation(Location l){
        if(location != null && !isGenerated)
            throw new IllegalStateException("This method should only be called once.");
        
        location = l;
        return this;
    }

    /**
     * The location of this Room.
     * @return The location of this Room.
     */
    public Location getLocation() {
        return location;
    }
    
    /**
     * Adds a link to this Room.
     * @param d the direction in which this link is
     * @param l the link itself
     * @return This room, to allow method-chaining.
     */
    public Room addLink(Direction d, Link l){
        if(isGenerated)
            throw new IllegalStateException("This room has already been generated,"
                    + " no new links can be added.");
        
        if(d == null)
            throw new IllegalArgumentException("The direction shouldn't be null.");
        
        if(l == null)
            throw new IllegalArgumentException("The link shouldn't be null.");
        
        if(neighbors.containsKey(d))
            throw new IllegalArgumentException("This room already contains a "
                    + "Link ("+neighbors.get(d)+") in that direction (" + d + ")"
                    + ", you cannot add: " + l);
        
        if(!l.links(this))
            throw new IllegalArgumentException("The provided link doesn't link"
                    + "to this room: " + l);
        
        neighbors.put(d, l);
        return this;
    }
    
    public void endGeneration(){
        if(world == null)
            throw new IllegalStateException("The world is missing, call #setWorld.");
        
        if(location == null)
            throw new IllegalStateException("The location is missing, call #setLocation.");
        
        isGenerated = true;
    }
    
    public char getChar(){
        return '+';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.neighbors);
        hash = 79 * hash + Objects.hashCode(this.description);
        hash = 79 * hash + Objects.hashCode(this.location);
        hash = 79 * hash + Objects.hashCode(this.world);
        return hash;
    }

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
        final Room other = (Room) obj;
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.neighbors, other.neighbors)) {
            return false;
        }
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        if (!Objects.equals(this.world, other.world)) {
            return false;
        }
        return true;
    }
    
}

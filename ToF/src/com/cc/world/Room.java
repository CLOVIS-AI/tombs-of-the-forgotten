/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world;

import com.cc.players.Entity;
import com.cc.world.links.Link;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An ingame room.
 * @author Ivan Canet
 */
public class Room {
    
    private final Map<Direction, Link> neighbors
            = new HashMap<>();
    
    private final String description;
    
    private Location location;
    private World world;
    
    private boolean isGenerated = false;
    
    public Room(String description){
        this.description = description;
    }
    
    public Room(String description, Location location, World world){
        this.description = description;
        this.location = location;
        this.world = world;
    }
    
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
    
    public final void endGeneration(){
        if(world == null)
            throw new IllegalStateException("The world is missing, call #setWorld.");
        
        if(location == null)
            throw new IllegalStateException("The location is missing, call #setLocation.");
        
        isGenerated = true;
    }
    
    public char getChar(){
        return '+';
    }
    
    /**
     * Gets a Neighboring Room of this Room.
     * @param d the Direction in which to search a neighbor
     * @return The neighboring room, or an empty Optional if none was found.
     */
    public Optional<Room> getNeighbor(Direction d) {
        if(!neighbors.containsKey(d))
            return Optional.empty();
        
        return Optional.ofNullable(
                neighbors.get(d).getOtherRoom(this)
            );
    }
    
    /**
     * Gets all the neighbors of this Room.
     * @return All the neighbors of this Room.
     * @see #getOpenNeighbors() The neighbors that are opened
     * @see #getReachableNeighbors(com.cc.players.Entity) The neighbors that an entity can go to
     */
    public Collection<Room> getAllNeighbors() {
        return neighbors.values()
                .stream()
                .map(l -> l.getOtherRoom(this))
                .collect(Collectors.toList());
    }
    
    /**
     * Gets the neighbors of this Room that are opened (any entity can go there).
     * @return The neighbors that are open.
     * @see #getAllNeighbors() All the neighbors
     * @see #getReachableNeighbors(com.cc.players.Entity) The neighbors that an entity can go to
     */
    public Collection<Room> getOpenNeighbors() {
        return neighbors.values()
                .stream()
                .filter(l -> l.isOpenned())
                .map(l -> l.getOtherRoom(this))
                .collect(Collectors.toList());
    }
    
    /**
     * Gets the neighbors of this Room that an entity can go to (open or the 
     * entity can open them).
     * @param e the entity
     * @return The neighbors that an Entity can reach.
     * @see #getAllNeighbors() All the neighbors
     * @see #getOpenNeighbors() The neighbors that are opened
     */
    public Collection<Room> getReachableNeighbors(Entity e) {
        return neighbors.values()
                .stream()
                .filter(l -> l.isOpenned() || l.canOpen(e))
                .map(l -> l.getOtherRoom(this))
                .collect(Collectors.toList());
    }
    
    /**
     * For a given Room, checks that it is a neighbor of this Room and returns
     * the Direction to take to get to it.
     * @param r the given room
     * @return The Direction you should take to get to the given Room.
     */
    public Direction getDirectionTo(Room r){
        for(Entry<Direction, Link> entry : neighbors.entrySet())
            if(entry.getValue().getOtherRoom(this).equals(r))
                return entry.getKey();
        
        throw new IllegalArgumentException("You provided a room that is not a "
                + "neighbor of this room.");
    }
    
    /**
     * Can an entity move from this Room in that direction?
     * @param d the direction
     * @return {@code true} if it can 
     */
    public boolean canMove(Direction d){
        return neighbors.containsKey(d) 
            && neighbors.get(d).isOpenned();
    }
    
    /**
     * Can an entity move from this Room to that Room?
     * @param r the room (must be a neighboring room)
     * @return {@code true} if it can 
     */
    public boolean canMove(Room r){
        return canMove(getDirectionTo(r));
    }
    
    /**
     * Can an entity open the link to a Room in that Direction?
     * @param d the direction
     * @param e the entity
     * @return {@code true} if it can
     */
    public boolean canOpen(Direction d, Entity e){
        return neighbors.containsKey(d)
            && neighbors.get(d).canOpen(e);
    }
    
    /**
     * Can an entity open the link to that Room?
     * @param r the room
     * @param e the entity
     * @return {@code true} if it can
     */
    public boolean canOpen(Room r, Entity e){
        return canOpen(getDirectionTo(r), e);
    }
    
    /**
     * Can an entity close the link to a Room in that Direction?
     * @param d the direction
     * @param e the entity
     * @return {@code true} if it can
     */
    public boolean canClose(Direction d, Entity e){
        return neighbors.containsKey(d)
            && neighbors.get(d).canClose(e);
    }
    
    /**
     * Can an entity close the link to that Room?
     * @param r the room
     * @param e the entity
     * @return {@code true} if it can
     */
    public boolean canClose(Room r, Entity e){
        return canClose(getDirectionTo(r), e);
    }
    
    /**
     * Opens the link in a direction.
     * @param d the direction
     * @param e the entity that wants to open the link
     * @return The state of the link after the call.
     */
    public boolean open(Direction d, Entity e){
        if(!neighbors.containsKey(d))
            throw new IllegalArgumentException("There is no neighbor in that "
                    + "Direction: " + d);
        
        return neighbors.get(d).open(e);
    }
    
    /**
     * Opens the link to a room.
     * @param r the room
     * @param e the entity that wants to open the link
     * @return The state of the link after the call.
     */
    public boolean open(Room r, Entity e){
        return open(getDirectionTo(r), e);
    }
    
    /**
     * Closes the link in a direction.
     * @param d the direction
     * @param e the entity that wants to close the link
     * @return The state of the link after the call.
     */
    public boolean close(Direction d, Entity e){
        if(!neighbors.containsKey(d))
            throw new IllegalArgumentException("There is no neighbor in that "
                    + "Direction: " + d);
        
        return neighbors.get(d).close(e);
    }
    
    /**
     * Closes the link to a Room.
     * @param r the room
     * @param e the entity that wants to close the link
     * @return The state of the link after the call.
     */
    public boolean close(Room r, Entity e){
        return close(getDirectionTo(r), e);
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

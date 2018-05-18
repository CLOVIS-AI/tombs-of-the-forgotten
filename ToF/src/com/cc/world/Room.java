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
package com.cc.world;

import com.cc.items.ItemContainer;
import com.cc.players.Entity;
import com.cc.utils.Save;
import com.cc.world.Path.UnreachableRoomException;
import com.cc.world.links.Link;
import com.eclipsesource.json.JsonObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * An ingame room.
 * @author Ivan Canet
 */
public class Room implements Save<JsonObject> {
    
    private final Map<Direction, Link> neighbors
            = new HashMap<>();
    
    private final String description;
    
    private Location location;
    private World world;
    
    private boolean isGenerated = false;
    
    private ItemContainer items;
    
    public Room(String description){
        this(description, null, null);
        isGenerated = false;
    }
    
    public Room(String description, Location location, World world){
        this.description = description;
        this.location = location;
        this.world = world;
        isGenerated = true;
        items = new ItemContainer(description);
    }
    
    /**
     * Loads a room from JSON. Do not forget that this doesn't load 
     * cross-references, so you have to call methods {@link #setLocation(com.cc.world.Location) }
     * and {@link #setWorld(com.cc.world.World) }.
     * @param json the saved data
     */
    public Room(JsonObject json){
        this(json.getString("desc", null));
        this.items = new ItemContainer(json.get("items").asObject());
        this.location = new Location(json.get("location").asObject());
    }
    
    /**
     * Sets the world of this object. This method can only be called once, during
     * the generation phase.
     * @param w the world of this object.
     * @return This room itself, to allow method-chaining.
     */
    public Room setWorld(World w){
        if(world != null || isGenerated)
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
        if(location != null || isGenerated){
            System.err.println("Warning: Room#setLocation should only be called"
                    + " once. The current value " + location + " is not changed"
                    + " to " + l + ".");
            return this;
        }
        
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
    public Stream<Room> getAllNeighbors() {
        return neighbors.values()
                .stream()
                .map(l -> l.getOtherRoom(this));
    }
    
    /**
     * Gets all the links of this Room. To get the neighbors, 
     * see {@link #getAllNeighbors() getAllNeighbors}.
     * @return A stream of all the links of this Room.
     */
    public Stream<Link> getAllLinks() {
        return neighbors.values().stream();
    }
    
    /**
     * Gets the neighbors of this Room that are opened (any entity can go there).
     * @return The neighbors that are open.
     * @see #getAllNeighbors() All the neighbors
     * @see #getReachableNeighbors(com.cc.players.Entity) The neighbors that an entity can go to
     */
    public Stream<Room> getOpenNeighbors() {
        return neighbors.values()
                .stream()
                .filter(l -> l.isOpenned())
                .map(l -> l.getOtherRoom(this));
    }
    
    /**
     * Gets the neighbors of this Room that an entity can go to (open or the 
     * entity can open them).
     * @param e the entity
     * @return The neighbors that an Entity can reach.
     * @see #getAllNeighbors() All the neighbors
     * @see #getOpenNeighbors() The neighbors that are opened
     */
    public Stream<Room> getReachableNeighbors(Entity e) {
        return neighbors.values()
                .stream()
                .filter(l -> l.isOpenned() || l.canOpen(e))
                .map(l -> l.getOtherRoom(this));
    }
    
    /**
     * For a given Room, checks that it is a neighbor of this Room and returns
     * the Direction to take to get to it.
     * @param r the given room
     * @return The Direction you should take to get to the given Room.
     */
    public Optional<Direction> getDirectionTo(Room r){
        for(Entry<Direction, Link> entry : neighbors.entrySet())
            if(entry.getValue().getOtherRoom(this).equals(r))
                return Optional.of(entry.getKey());
        
        return Optional.empty();
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
        return canMove(getDirectionTo(r)
                .orElseThrow(()->new IllegalArgumentException("The provided"
                        + "room is not a neighbor of this room!")));
    }
    
    /**
     * Can an entity reach the room in that direction?
     * <p>An entity can reach the room if it's openned or if it can open it.
     * @param d the direction
     * @param e the entity
     * @return {@code true} if it can 
     */
    public boolean canReach(Direction d, Entity e){
        return neighbors.containsKey(d) 
            && (neighbors.get(d).isOpenned() || neighbors.get(d).canOpen(e));
    }
    
    /**
     * Can an entity reach that Room?
     * <p>An entity can reach the room if it's openned or if it can open it.
     * @param r the room (must be a neighboring room)
     * @param e the entity
     * @return {@code true} if it can 
     */
    public boolean canReach(Room r, Entity e){
        return canReach(getDirectionTo(r)
                .orElseThrow(()->new IllegalArgumentException("The provided"
                        + "room is not a neighbor of this room!")), e);
    }
    
    /**
     * Creates a path to an other room.
     * <p>This method uses the Dijsktra algorithm to find the shortest path, see
     * {@link Path#createPath(com.cc.world.World, com.cc.world.Room, com.cc.world.Room, com.cc.players.Entity) Path.createPath}.
     * @param target the room the path will lead to
     * @param entity the entity that takes this path
     * @return A path to the other room.
     * @throws com.cc.world.Path.UnreachableRoomException If no path exists
     * between the two rooms.
     */
    public Path pathTo(Room target, Entity entity) throws UnreachableRoomException {
        if(world == null)
            throw new NullPointerException("The world has not assigned. See Room#setWorld.");
        
        return Path.createPath(world, this, target, entity);
    }
    
    /**
     * Can the entity move from this room to the other one?
     * <p>This method tries to generate a path from this Room to the other one
     * using {@link #pathTo(com.cc.world.Room, com.cc.players.Entity) pathTo},
     * and returns {@code true} or {@code false} whether it succeeded or not.
     * @param target the target room
     * @param e the entity
     * @return {@code true} if the specified entity can move from this room to
     * the other one.
     */
    public boolean canMove(Room target, Entity e){
        try {
            pathTo(target, e);
            return true;
        } catch (UnreachableRoomException ex) {
            return false;
        }
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
        return canOpen(getDirectionTo(r).orElseThrow(()->new IllegalArgumentException("The provided"
                        + "room is not a neighbor of this room!")), e);
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
        return canClose(getDirectionTo(r).orElseThrow(()->new IllegalArgumentException("The provided"
                        + "room is not a neighbor of this room!")), e);
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
     * Opens the link in a direction.
     * @param d the direction
     * @param e the entity that wants to open the link
     * @return The state of the link after the call.
     */
    public boolean open(Optional<Direction> d, Entity e){
        return open(d.get(), e);
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
     * Closes the link in a direction.
     * @param d the direction
     * @param e the entity that wants to close the link
     * @return The state of the link after the call.
     */
    public boolean close(Optional<Direction> d, Entity e){
        return close(d.get(), e);
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
    
    public ItemContainer getItems() {
        return items;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.neighbors);
        hash = 79 * hash + Objects.hashCode(this.description);
        hash = 79 * hash + Objects.hashCode(this.location);
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
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append("\"")
                .append(description)
                .append("\" ")
                .append(location)
                .append(" [");
        
        neighbors.forEach((d,l) -> sb
                .append(" ")
                .append(d)
                .append(" ")
                .append(l.getOtherRoom(this).location));
        
        return sb.append("]").toString();
    }

    @Override
    public JsonObject save() {
        return new JsonObject()
                .add("desc", description)
                .add("items", items.save())
                .add("location", location.save());
    }
    
}

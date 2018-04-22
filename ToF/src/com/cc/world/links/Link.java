/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world.links;

import com.cc.players.Entity;
import com.cc.world.Room;
import java.util.Arrays;
import java.util.List;

/**
 * The link between two Rooms (eg. a door).
 * @author Ivan Canet
 */
public abstract class Link {
    
    private final Room room1,
                       room2;
    
    /** Is used to decide whether this link is open or not. */
    protected boolean isOpen;
    
    /**
     * Creates a link between two rooms.
     * @param r1 the first room
     * @param r2 the second room
     * @param openByDefault {@code true} if the door is opened by default
     */
    public Link(Room r1, Room r2, boolean openByDefault){
        if(r1 == null)      
            throw new IllegalArgumentException("r1 cannot be null.");
        if(r2 == null)      
            throw new IllegalArgumentException("r2 cannot be null.");
        
        room1 = r1;
        room2 = r2;
        isOpen = openByDefault;
    }
    
    /**
     * Returns the two rooms as an array.
     * @return An array of the two rooms linked by this object.
     */
    public Room[] getRoomsArray(){
        return new Room[]{room1, room2};
    }
    
    /**
     * Returns the two rooms linked by this object.
     * @return The two rooms linked by this object.
     */
    public List<Room> getRooms(){
        return Arrays.asList(room1, room2);
    }
    
    /**
     * Does this link links to this room?
     * @param r the room
     * @return {@code true} if the specified room is linked by this object.
     */
    public boolean links(Room r){
        return room1.equals(r) || room2.equals(r);
    }
    
    /**
     * Used by a room to get the other room.
     * @param r one of the rooms that this object links to
     * @return The other room linked by this object.
     */
    public Room getOtherRoom(Room r){
        if(r.equals(room1))
            return room2;
        else if(r.equals(room2))
            return room1;
        else
            throw new IllegalArgumentException("The provided room is neither of "
                    + "the rooms contained in this link: " + r.toString());
    }
    
    /**
     * Is this link open?
     * @return {@code true} if it is open.
     */
    public boolean isOpenned(){
        return isOpen;
    }
    
    /**
     * Can an entity open that link?
     * @param e the entity that wants to open the link
     * @return {@code true} if the entity can call {@link #open(com.cc.players.Entity) }.
     */
    public abstract boolean canOpen(Entity e);
    
    /**
     * Can an entity close that link?
     * @param e the entity that wants to close the link
     * @return {@code true} if the entity can call {@link #close(com.cc.players.Entity) }.
     */
    public abstract boolean canClose(Entity e);
    
    /**
     * Opens this link.
     * @param e the entity that wants to open it
     * @return The state of the link after the call; {@code true} if it is open.
     */
    public abstract boolean open(Entity e);
    
    /**
     * Closes this link.
     * @param e the entity that wants to close it
     * @return The state of the link after the call; {@code true} if it is open.
     */
    public abstract boolean close(Entity e);
    
}

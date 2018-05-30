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
package com.cc.world.links;

import com.cc.players.Entity;
import com.cc.utils.Save;
import com.cc.utils.messages.Message;
import com.cc.world.Direction;
import static com.cc.world.Direction.fromCoordinates;
import com.cc.world.Location;
import com.cc.world.Room;
import com.cc.world.World;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The link between two Rooms (eg. a door).
 * <p>Because two rooms cannot be linked together by two links, this object's
 * {@link #equals(java.lang.Object) } method will return {@code true} for two 
 * links that share the same two Rooms (no matter the order).
 * @author Ivan Canet
 */
public abstract class Link implements Save<JsonObject> {
    
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
     * Loads a link from a JSON object, and auto-links itself with both rooms.
     * To avoid cross-references, the JSON data of a link only contains the
     * location of both rooms it is linked to, therefore you need to provide the
     * world object to find the actual room object. It is expected that the
     * rooms were loaded first, so they are already contained by the World object.
     * @param world the world object
     * @param json the saved data of this link
     */
    protected Link(World world, JsonObject json){
        this(world.getRoom(new Location(json.get("room1").asObject()))
                    .orElseThrow(() ->  new IllegalArgumentException("No room "
                            + "was found where room1 was expected: "
                            + json.toString(WriterConfig.PRETTY_PRINT))),
                
             world.getRoom(new Location(json.get("room2").asObject()))
                    .orElseThrow(() ->  new IllegalArgumentException("No room "
                            + "was found where room2 was expected: " 
                            + json.toString(WriterConfig.PRETTY_PRINT))),
             
             json.getBoolean("isOpen", false));
        
        autoLink();
    }
    
    /**
     * Creates a link between two rooms, that is open by default.
     * @param rooms the two rooms (in no particular order)
     */
    public Link(Room... rooms){
        this(rooms[0], rooms[1]);
    }
    
    /**
     * Creates a link between two rooms, that is open by default.
     * @param r1 the first room
     * @param r2 the second room
     */
    public Link(Room r1, Room r2){
        this(r1, r2, true);
    }
    
    /**
     * Returns the two rooms as an array.
     * @return An array of the two rooms linked by this object.
     */
    public final Room[] getRoomsArray(){
        return new Room[]{room1, room2};
    }
    
    /**
     * Returns the two rooms linked by this object.
     * @return The two rooms linked by this object.
     */
    public final List<Room> getRooms(){
        return Arrays.asList(room1, room2);
    }
    
    /**
     * Does this link links to this room?
     * @param r the room
     * @return {@code true} if the specified room is linked by this object.
     */
    public final boolean links(Room r){
        return room1.equals(r) || room2.equals(r);
    }
    
    /**
     * Links this Link to both of its Rooms.
     * @return the direction that was found to reach the two rooms (not that 
     * there is no specified order, that is, for one of the rooms, the direction
     * needed to go from it to the next one is either the returned direction or
     * {@link Direction#getOpposite() its opposite}).
     */
    public final Direction autoLink(){
        Direction d = fromCoordinates(room2.getLocation().remove(room1.getLocation()));
        
        room1.addLink(d, this);
        room2.addLink(d.getOpposite(), this);
        
        return d;
    }
    
    /**
     * Used by a room to get the other room.
     * @param r one of the rooms that this object links to
     * @return The other room linked by this object.
     */
    public final Room getOtherRoom(Room r){
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
    public final boolean isOpenned(){
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
    public final boolean open(Entity e){
        if(!isOpen && canOpen(e)){
            isOpen = true;
            room1.getWorld().newMessage(new Message().add("You opened the door."));
        }
        return isOpen;
    }
    
    /**
     * Closes this link.
     * @param e the entity that wants to close it
     * @return The state of the link after the call; {@code true} if it is open.
     */
    public final boolean close(Entity e){
        if(isOpen && canClose(e))
            isOpen = false;
        return isOpen;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = hash + Objects.hashCode(this.room1.getLocation()) * 89;
        hash = hash + Objects.hashCode(this.room2.getLocation()) * 89;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        
        final Link other = (Link) obj;
        if(room1.getLocation().equals(other.room1.getLocation())
        && room2.getLocation().equals(other.room2.getLocation()))
            return true;
        if(room1.getLocation().equals(other.room2.getLocation())
        && room2.getLocation().equals(other.room1.getLocation()))
            return true;
        return false;
    }
    
    public static Link loadLink(World world, JsonObject json){
        switch(json.getString("type", null)){
            case "door":        return new Door(world, json);
            case "openning":    return new Opening(world, json);
            case "key":         return new KeyDoor(world, json);
            default:
                throw new IllegalArgumentException("Cannot find the field 'type'"
                        + "in the provided JSON: " + json);
        }
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + " between " + room1 + " and "
             + room2 + ", currently " + (isOpen ? "unlocked" : "locked") + ".";
    }

    @Override
    public JsonObject save() {
        return new JsonObject()
                .add("room1", room1.getLocation().save())
                .add("room2", room2.getLocation().save())
                .add("isOpen", isOpen);
    }
    
}

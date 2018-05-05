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

import com.cc.players.Entity;
import com.cc.players.Player;
import com.cc.utils.Save;
import com.cc.utils.messages.Message;
import com.cc.world.links.Link;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * The world in which the game is taking place.
 * @author Ivan Canet
 */
public class World implements Timable, Save<JsonObject> {
    
    // Package private to enable access for the tests
    final TreeMap<Location, Room> rooms;
    
    private final Player player;
    
    private final List<Entity> entities;
    
    private GameState gameState = GameState.EXPLORE;
    
    private Queue<Message> messages;
    
    // ************************************************* C O N S T R U C T O R S
    
    @SuppressWarnings("LeakingThisInConstructor")
    public World(TreeMap<Location, Room> map, Player player){
        map.forEach((l, r) -> {
            r.setWorld(this);
            r.setLocation(l);
        });
        
        rooms = map;
        this.player = player;
        this.player.setWorld(this);
        this.entities = new ArrayList();
    }
    
    /**
     * Creates a World object from JSON.
     * @param json the saved data
     */
    public World(JsonObject json){
        rooms = extractRooms(json.get("rooms").asArray());
        extractAndIncludeLinks(json.get("links").asArray());
        
        player = new Player(json.get("player").asObject());
        entities = extractEntities(json.get("entities").asArray());
        
        gameState = GameState.valueOf(json.getString("state", null));
        
        concludeLoading();
    }
    
    final void concludeLoading(){
        rooms.values().forEach(Room::endGeneration);
        player.setWorld(this);
        entities.forEach(e -> e.setWorld(this));
    }
    
    final TreeMap<Location, Room> extractRooms(JsonArray json){
        TreeMap<Location, Room> map = new TreeMap<>();
        for(JsonValue j : json.values()){
            Room r = new Room(j.asObject());
            r.setWorld(this);
            map.put(r.getLocation(), r);
        }
        return map;
    }
    
    final List<Entity> extractEntities(JsonArray json){
        List<Entity> list = new ArrayList<>(json.size());
        for(JsonValue j : json.values()){
            // @TODO in #78: Add the entity
        }
        return list;
    }
    
    final void extractAndIncludeLinks(JsonArray json){
        json.values().stream()
                .map(JsonValue::asObject)
                .forEach(j -> Link.loadLink(this, j));
    }
    
    // ****************************************************** G A M E  L O G I C
    
    /**
     * Notifies every component of the World that a tick has passed.
     */
    @Override
    public void nextTick(){
        player.nextTick();
        entities.forEach(e -> e.nextTick());
    }
    
    // *********************************************************** G E T T E R S
    
    /**
     * The player of the game.
     * @return The player of the game.
     */
    public Player getPlayer(){
        return player;
    }
    
    /**
     * The state of the game.
     * @return The state of the game.
     */
    public GameState getGameState(){
        return gameState;
    }
    
    /**
     * Get every room of the World.
     * <p>Note that the rooms are provided in no particular order.
     * @return A Collection of rooms.
     */
    public Collection<Room> getRooms(){
        return rooms.values();
    }
    
    /**
     * Gets a Room in this World.
     * @param location the location you want
     * @return The room that is found at that Location, or an empty Optional if
     *         no Room is found there.
     */
    public Optional<Room> getRoom(Location location){
        return Optional.ofNullable(rooms.get(location));
    }
    
    /**
     * Get the rooms which location validate a predicate.
     * <p>Note that the rooms are provided in no particular order.
     * <p>This method returns a Stream that has not yet been executed, meaning
     * that, thanks to the lazy execution of Streams, this method call is
     * virtually free.
     * @param p a predicate on the location of each room
     * @return The rooms.
     */
    public Stream<Room> selectRoomsByLocation(Predicate<Location> p){
        return rooms.keySet()
                .stream()
                .filter(p)
                .map(rooms::get);
    }
    
    /**
     * Get the rooms that validate a predicate.
     * <p>Note that the rooms are provided in no particular order.
     * <p>This method returns a Stream that has not yet been executed, meaning
     * that, thanks to the lazy execution of Streams, this method call is
     * virtually free.
     * @param p a predicate on the room
     * @return The rooms.
     */
    public Stream<Room> selectRooms(Predicate<Room> p){
        return rooms.values()
                .stream()
                .filter(p);
    }
    
    /**
     * Gets the entities according to a predicate. The player is ignored.
     * <p>This method returns a Stream that has not yet been executed, meaning
     * that, thanks to the lazy execution of Streams, this method call is
     * virtually free.
     * @param p how to choose the entities
     * @return The selected entities.
     */
    public Stream<Entity> selectEntities(Predicate<Entity> p){
        return entities
                .stream()
                .filter(p);
    }
    
    /**
     * Gets the entities that are located in a specific floor.
     * <p>This method returns a Stream that has not yet been executed, meaning
     * that, thanks to the lazy execution of Streams, this method call is
     * virtually free.
     * @param floor the floor you want
     * @return The entities on that floor.
     */
    public Stream<Entity> selectEntitiesByFloor(int floor){
        return entities
                .stream()
                .filter(e -> e.getLocation().getZ() == floor);
    }
    
    /**
     * Get the map of a specific floor.
     * @param floor The floor you want [0..]
     * @return A Map of every room and its location.
     */
    public TreeMap<Location, Room> getFloor(int floor){
        if(floor < 0)
            throw new IllegalArgumentException("Negative values are forbidden "
                    + "in this method, you provided: " + floor);
        
        return rooms.entrySet()
                .stream()
                .filter(e -> e.getKey().getZ() == floor)
                .collect(
                        TreeMap::new, 
                        (TreeMap m, Entry e) -> m.put(e.getKey(), e.getValue()), 
                        TreeMap::putAll);
    }
    
    public String floorToString(final int floor){
        StringBuilder sb = new StringBuilder();
        
        TreeMap<Location, Room> floorMap = new TreeMap<>(getFloor(floor));
        
        int x = 0, y = 0;
        for(Entry<Location, Room> e : floorMap.entrySet()){
            Location l = e.getKey();
            
            // Next line if needed
            while(x < l.getX()){
                sb.append('\n');
                x++;
                y = 0;
            }
            
            // Jump column if needed
            while(y < l.getY()){
                sb.append(' ');
                y++;
            }
            
            if(player.getLocation().equals(l)){
                // Draw the player
                sb.append("@");
            }else{
                // Draw room
                sb.append(e.getValue().getChar());
            }
            y++;
        }
        
        return sb.toString();
    }
    
    /**
     * Can the player move in that direction?
     * @param direction the direction
     * @return {@code true} if the player can move in that direction.
     */
    public final boolean canMove(Direction direction){
        return player.canMoveTo(direction);
    }
    
    /**
     * Moves the player in a direction.
     * @param direction the direction
     */
    public final void movePlayer(Direction direction){
        if(canMove(direction))
            player.moveTo(direction);
        else
            System.out.println("Cannot move in this direction.");
    }
    
    /**
     * Adds a message to the queue of messages (story messages...).
     * @param message the message
     */
    public final void newMessage(Message message){
        messages.add(message);
    }
    
    /**
     * Gets a message from the queue of messages. That message is then deleted
     * from the queue.
     * @return A message from the queue.
     */
    public final Message getNextMessage() {
        return messages.remove();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.rooms);
        hash = 71 * hash + Objects.hashCode(this.player);
        hash = 71 * hash + Objects.hashCode(this.entities);
        hash = 71 * hash + Objects.hashCode(this.gameState);
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
        final World other = (World) obj;
        if (!Objects.equals(this.rooms, other.rooms)) {
            return false;
        }
        if (!Objects.equals(this.player, other.player)) {
            return false;
        }
        if (!Objects.equals(this.entities, other.entities)) {
            return false;
        }
        if (this.gameState != other.gameState) {
            return false;
        }
        return true;
    }

    @Override
    public JsonObject save() {
        JsonArray jrooms = new JsonArray();
        rooms.values().forEach(r -> jrooms.add(r.save()));
        
        JsonArray jlinks = new JsonArray();
        rooms.values()
                .stream()
                .flatMap(r -> r.getAllLinks())
                .distinct()
                .forEach(l -> jlinks.add(l.save()));
        
        JsonArray jentities = new JsonArray();
        entities.forEach(e -> jentities.add(e.save()));
        
        return new JsonObject()
                .add("rooms", jrooms)
                .add("links", jlinks)
                .add("player", player.save())
                .add("state", gameState.toString())
                .add("entities", jentities);
    }
}

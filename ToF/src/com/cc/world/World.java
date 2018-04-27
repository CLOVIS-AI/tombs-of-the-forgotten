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
import com.cc.utils.messages.Message;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The world in which the game is taking place.
 * @author Ivan Canet
 */
public class World implements Timable {
    
    private TreeMap<Location, Room> rooms;
    
    private Player player;
    
    private List<Entity> entities;
    
    private GameState gameState;
    
    private Queue<Message> messages;
    
    // ************************************************* C O N S T R U C T O R S
    
    public World(TreeMap<Location, Room> map, Player player){
        map.forEach((l, r) -> r.setWorld(this));
        
        rooms = map;
        this.player = player;
        this.player.setWorld(this);
    }
    
    /**
     * Creates a new World (random generation).
     */
    public World(){
        this(System.currentTimeMillis());
    }
    
    /**
     * Creates a new World (random generation) based on a seed.
     * @param seed The generation seed.
     */
    public World(final long seed){
        throw new UnsupportedOperationException();
    }
    
    /**
     * Loads a World object from a file.
     * <p>Note that this contructor is meant for the Prototype only, it will
     * marked as deprecated in any other releases (JSON will be used instead).
     * @param f the file
     */
    public World(File f){
        rooms = new TreeMap<>();
        
        System.out.println("Loading world from file " + f.getAbsolutePath());
        try {
            Scanner s = new Scanner(f);
            
            int y = 0, 
                z = 0;
            
            while(s.hasNextLine()){
                String line = s.nextLine();
                
                switch(line){
                    case "-":   y=0; z++; break;
                    case "":    continue;
                }
                
                int x = 0;
                for(char c : line.toCharArray()){
                    switch(c){
                        case '@':
                            player = new Player();
                        case '+':
                            rooms.put(new Location(x, y, z), new Room("+").setWorld(this));
                            break;
                        default:
                    }
                    x++;
                }
            }
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("The file doesn't exist: " + f.getAbsolutePath());
        }
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
     * @param p a predicate on the location of each room
     * @return The rooms.
     */
    public List<Room> selectRoomsByLocation(Predicate<Location> p){
        return rooms.keySet()
                .stream()
                .filter(p)
                .map(rooms::get)
                .collect(Collectors.toList());
    }
    
    /**
     * Get the rooms that validate a predicate.
     * <p>Note that the rooms are provided in no particular order.
     * @param p a predicate on the room
     * @return The rooms.
     */
    public List<Room> selectRooms(Predicate<Room> p){
        return rooms.values()
                .stream()
                .filter(p)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets the entities according to a predicate. The player is ignored.
     * @param p how to choose the entities
     * @return The selected entities.
     */
    public List<Entity> selectEntities(Predicate<Entity> p){
        return entities
                .stream()
                .filter(p)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets the entities that are located in a specific floor.
     * @param floor the floor you want
     * @return The entities on that floor.
     */
    public List<Entity> selectEntitiesByFloor(int floor){
        return entities
                .stream()
                .filter(e -> e.getLocation().getZ() == floor)
                .collect(Collectors.toList());
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
    
    public final boolean canMove(Location location, Direction direction){
        return rooms.containsKey(location.add(direction));
    }
    
    /**
     * Moves the player in a direction.
     * @param direction the direction
     */
    public final void movePlayer(Direction direction){
        if(canMove(player.getLocation(), direction))
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
}

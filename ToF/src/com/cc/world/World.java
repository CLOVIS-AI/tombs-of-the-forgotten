/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world;

import com.cc.players.Entity;
import com.cc.players.Player;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The world in which the game is taking place.
 * @author Ivan Canet
 */
public class World {
    
    private TreeMap<Location, Room> rooms;
    
    private Player player;
    
    private List<Entity> entities;
    
    private GameState gameState;
    
    // ************************************************* C O N S T R U C T O R S
    
    public World(TreeMap<Location, Room> map){
        rooms = map;
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
                            rooms.put(new Location(x, y, z), new Room());
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
    
    
    
    // *********************************************************** G E T T E R S
    
    /**
     * Get every room of the World.
     * <p>Note that the rooms are provided in no particular order.
     * @return A Collection of rooms.
     */
    public Collection<Room> getRooms(){
        return rooms.values();
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
            
            // Draw room
            sb.append(e.getValue().getChar());
            y++;
        }
        
        return sb.toString();
    }
}

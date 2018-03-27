/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world;

import com.cc.players.Entity;
import com.cc.players.Player;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
    
    /**
     * Get every room of the World.
     * @return A Collection of rooms.
     */
    public Collection<Room> getRooms(){
        return rooms.values();
    }
    
    /**
     * Get the rooms which location validate a predicate.
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
    public Map<Location, Room> getFloor(int floor){
        if(floor < 0)
            throw new IllegalArgumentException("Negative values are forbidden "
                    + "in this method, you provided: " + floor);
        
        return rooms.entrySet()
                .stream()
                .filter(e -> e.getKey().getZ() == floor)
                .collect(Collectors.toMap(
                        e -> e.getKey(), 
                        e -> e.getValue()));
    }
}

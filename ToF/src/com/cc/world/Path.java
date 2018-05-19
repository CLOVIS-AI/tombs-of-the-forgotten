/*
 * The MIT License
 *
 * Copyright 2018 Canet Ivan & Chourouq Sarah.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cc.world;

import com.cc.players.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Stream;

/**
 * This class represents a path from a Room to an other. It is represented as a
 * Queue of Rooms: you can get the next Room where you'd like to go, etc.
 * @author Ivan Canet
 */
public class Path {
    
    private final Stack<Room> rooms;
    
    private Path(Stack<Room> stack){
        rooms = stack;
        
    }
    
    /**
     * Removes the next room from this path and returns it.
     * @return The next step.
     */
    public Room moveToNext(){
        return rooms.pop();
    }
    
    /**
     * Returns a stream of the Rooms of this Path.
     * @return A stream of the Rooms of this Path.
     */
    public Stream<Room> seePath(){
        return rooms.stream();
    }
    
    /**
     * Verifies that this Path is still valid.
     * <p>A path can become invalid if the entity cannot open doors, and that this
     * path goes through an open door that has been closed since the path's generation.
     * @param entity the entity
     * @return {@code true} if this path is still valid.
     */
    public boolean checkPathValidity(Entity entity){
        List<Room> list = rooms.subList(0, rooms.size());
        for(int i = 0; i < list.size()-1; i++)
            if(!list.get(i).canReach(list.get(i+1), entity))
                return false;
        return true;
    }
    
    /**
     * Creates a path using the Dijsktra algorithm.
     * @param world the world
     * @param departure the room you'd like to go from
     * @param arrival the room you'd like to go to
     * @param entity the entity that will take the path
     * @return The shortest path from the first room to the target.
     * @throws com.cc.world.Path.UnreachableRoomException If no path is found.
     */
    public static Path createPath(World world, Room departure, Room arrival, Entity entity) throws UnreachableRoomException{
        // Initilization of costs (any=Infinity, first=0)
        Map<Room, Integer> costs = new HashMap<>();
        world.getRooms().forEach(r -> costs.put(r, Integer.MAX_VALUE));
        costs.put(departure, 0);
        
        // Initilization of predecessors (any=itself)
        Map<Room, Room> predecessors = new HashMap<>();
        world.getRooms().forEach(r -> predecessors.put(r, r));
        
        // Search for shortcuts
        List<Room> unterminated = new ArrayList<>(world.getRooms());
        while(!unterminated.isEmpty()){
            
            //System.out.println("r          \tcost\tpred    \tterm");
            //world.getRooms().forEach(r -> System.out.println(r+"\t"+costs.get(r)+"\t"+predecessors.get(r)+"\t"+(unterminated.contains(r) ? "" : "x")));
            
            Room min = unterminated.stream()
                    .min((r1, r2) -> costs.get(r1) - costs.get(r2))
                    .orElseThrow(UnknownError::new);
            
            min.getReachableNeighbors(entity)
                    .filter(r -> unterminated.contains(r))
                    .filter(r -> costs.get(min) + 1 < costs.get(r))
                    .forEach(r -> {
                        costs.put(r, costs.get(min) + 1);
                        predecessors.put(r, min);
                    });
            
            unterminated.remove(min);
        }
        
        //System.out.println("Analyzing path...");
        
        Stack<Room> path = new Stack<>();
        do{
            //System.out.println("> " + arrival);
            path.add(arrival);
            
            if(arrival == predecessors.get(arrival))
                break;
            
            arrival = predecessors.get(arrival);
        }while(arrival != null);
        
        if(path.peek() != departure)
            throw new UnreachableRoomException(departure, arrival, path);
        
        return new Path(path);
    }
    
    /**
     * Is thrown when no path is found between two rooms.
     */
    public static class UnreachableRoomException extends Exception {
        public UnreachableRoomException(Room depart, Room target, Stack<Room> path){
            super("Cannot go from "+depart+" to "+target+"; path found:"+printStack(path));
        }
        private static String printStack(Stack<Room> stack){
            StringBuilder sb = new StringBuilder();
            stack.forEach(e -> sb.append(e));
            return sb.toString();
        }
    }
}

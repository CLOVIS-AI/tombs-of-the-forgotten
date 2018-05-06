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

/**
 * This class represents a path from a Room to an other. It is represented as a
 * Queue of Rooms: you can get the next Room where you'd like to go, etc.
 * @author Ivan Canet
 */
public class Path {
    
    private final Stack<Room> rooms;
    
    public Path(Stack<Room> stack){
        rooms = stack;
        
    }
    
    public Room moveToNext(){
        return rooms.pop();
    }
    
    public static Path createPath(World world, Room departure, Room arrival, Entity entity) throws UnreachableRoomException{
        // Initilization of costs (any=Infinity, first=0)
        Map<Room, Integer> costs = new HashMap<>();
        world.getRooms().forEach(r -> costs.put(r, 99));
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
        
        Stack path = new Stack();
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
    
    public static class UnreachableRoomException extends Exception {
        public UnreachableRoomException(Room depart, Room target, Stack<Room> path){
            super("Cannot go from "+depart+" to "+target+"; path found:"+printStack(path));
        }
        public static String printStack(Stack<Room> stack){
            StringBuilder sb = new StringBuilder();
            stack.forEach(e -> sb.append(e));
            return sb.toString();
        }
    }
}

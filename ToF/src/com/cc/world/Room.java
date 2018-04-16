/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world;

import java.util.Map;

/**
 * An ingame room.
 * @author Ivan Canet
 */
public class Room {
    
    private Map<Direction, Room> neighbors;
    
    private String description;
    
    private World world;
    
    /**
     * Sets the world of this object. This method can only be called once.
     * @param w the world of this object.
     * @return This room itself, to allow method-chaining.
     */
    public Room setWorld(World w){
        if(world != null)
            throw new IllegalStateException("This method should only be called once.");
        
        world = w;
        return this;
    }
    
    public char getChar(){
        return '+';
    }
    
}

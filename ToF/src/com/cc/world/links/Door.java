/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world.links;

import com.cc.players.Entity;
import com.cc.world.Room;

/**
 * A Door is a Link that can be opened by any entity.
 * @author Ivan Canet
 */
public class Door extends Link {

    /**
     * Creates a closed door.
     * @param r1 one of the linked rooms
     * @param r2 the other linked room
     */
    public Door(Room r1, Room r2) {
        this(r1, r2, false);
    }
    
    /**
     * Creates a door.
     * @param r1 one of the linked rooms
     * @param r2 the other linked room
     * @param openByDefault {@code true} if the door is opened
     */
    public Door(Room r1, Room r2, boolean openByDefault) {
        super(r1, r2, openByDefault);
    }

    @Override
    public boolean canOpen(Entity e) {
        return true;
    }

    @Override
    public boolean canClose(Entity e) {
        return true;
    }

    @Override
    public boolean open(Entity e) {
        return isOpen = true;
    }

    @Override
    public boolean close(Entity e) {
        return isOpen = false;
    }
    
}
